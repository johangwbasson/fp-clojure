# fp-clojure

Basic rest api with authentication in clojure

## Instructions

Run with

	lein run

Authenticate:

	curl -X POST -d '{"email":"admin", "password":"admin"}' http://localhost:7125/uthenticate
