(ns barewamp.websocket
  (:use [barewamp.config :only [conf]])
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :as http-kit]
            [clj-wamp.server :as wamp]))

;; Topic BaseUrls
(def base-url "http://barewamp")
(def evt-base-url (str base-url "/event#"))
(defn evt-url [path] (str evt-base-url path))

;; HTTP Kit/WAMP WebSocket handler

(defn- on-open [sess-id]
  (log/info "WAMP client connected [" sess-id "]"))

(defn- on-close [sess-id status]
  (log/info "WAMP client disconnected [" sess-id "] " status))

(defn- on-publish [sess-id topic event exclude include]
  (log/info "WAMP publish:" sess-id topic event exclude include))

(defn- auth-secret [sess-id auth-key extra]
  "Returns the auth key's secret (ie. password), typically retrieved from a database."
  "secret-password")

(defn- auth-permissions
  "Returns the permissions for a client session by auth key."
  [sess-id auth-key]
  {:subscribe {(evt-url "chat")  true}
   :publish   {(evt-url "chat")  true}})

(defn wamp-handler
  "Returns a http-kit websocket handler with wamp subprotocol"
  [req]
  (wamp/with-channel-validation req channel (:ws-origins-re (conf))
    (wamp/http-kit-handler channel
      {:on-open        on-open
       :on-close       on-close
       :on-subscribe   {(evt-url "chat")  true}
       :on-publish     {(evt-url "chat")  true
                        :on-after         on-publish}
       :on-auth        {:secret           auth-secret
                        :permissions      auth-permissions}})))


