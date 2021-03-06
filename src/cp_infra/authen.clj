(ns cp-infra.authen
  (:require [io.pedestal.http.body-params :as body-params]
            [ring.util.response :refer [response redirect status]]
            [io.pedestal.interceptor :as interceptor :refer [interceptor definterceptorfn defon-request defon-response defmiddleware]]
            [cemerick.friend :as friend]
            [cemerick.friend [credentials :as creds] [workflows :as workflows]]
            [io.pedestal.http.ring-middlewares :as middlewares]
            [ring.middleware.session.cookie :as cookie]
            [hiccup.page :as h]
            [hiccup.element :as e]))

(def users
  "root/clojure login using bcrypt."
  (let [password (creds/hash-bcrypt "clojure")]
    {"root" {:username "root" :password password :roles #{:cp-infra.authen/admin}}}))


(def friend-config
  "A friend config for interactive form use."
  {:login-uri           "/login"
   :allow-anon? true
   :default-landing-uri "/messages"
   :unauthorized-handler #(-> (h/html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                              response
                              (status 401))
   :workflows           [(workflows/interactive-form)]
   :credential-fn       (partial creds/bcrypt-credential-fn users)})

(definterceptorfn friend-authenticate-interceptor
                  "Creates a friend/authenticate interceptor for a given config."
                  [auth-config]
                  (interceptor
                    :error
                    (fn [{:keys [request] :as context} exception]
                      ;; get exception details without Slingshot in this sample
                      (let [exdata (ex-data exception)
                            extype (-> exdata :object :cemerick.friend/type)]
                        (if (#{:unauthorized} extype)
                          ;; unauthorized errors should generate a response using catch handler
                          (let [handler-map (:friend/handler-map request)
                                response ((:catch-handler handler-map) ;handler to use
                                          (assoc (:request handler-map) ;feed exception back in
                                                 :cemerick.friend/authorization-failure exdata))]
                            ;; respond with generated response
                            (assoc context :response response))
                          ;; re-throw other errors
                          (throw exception))))
                    :enter
                    (fn [{:keys [request] :as context}]
                      (let [response-or-handler-map
                            (friend/authenticate-request request auth-config)]
                        ;; a handler-map will exist in authenticated request if authenticated
                        (if-let [handler-map (:friend/handler-map response-or-handler-map)]
                          ;; friend authenticated the request, so continue
                          (assoc-in context [:request :friend/handler-map] handler-map)
                          ;; friend generated a response instead, so respond with it
                          (assoc context :response response-or-handler-map))))
                    :leave
                    ;; hook up friend response handling
                    (middlewares/response-fn-adapter friend/authenticate-response)))

(definterceptorfn friend-authorize-interceptor
                  "Creates a friend interceptor for friend/authorize."
                  [roles]
                  (interceptor
                    :enter
                    (fn [{:keys [request] :as context}]
                      (let [auth (:auth (:friend/handler-map request))]
                        ;; check user has an authorized role
                        (if (friend/authorized? roles auth)
                          ;; authorized, so continue
                          context
                          ;; unauthorized, so throw exception for authentication interceptor
                          (friend/throw-unauthorized auth {:cemerick.friend/required-roles
                                                           roles}))))))

