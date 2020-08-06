package main

import (
	"fmt"
	"net/http"
	"strings"
)

func main() {
	map_ := loadMap()
	startServer(map_)
}

func loadMap() map[string]string {
	m := make(map[string]string)
	m["leads"] = "http://localhost:8080/data/leads"
	m["rules"] = "http://localhost:8080/data/rules"
	return m
}

func startServer(map_ map[string]string) {
	const PORT = "8888"
	fmt.Println("Gateway serving: ")
	for k, v := range map_ {
		fmt.Printf("  %s ==> %s\n", k, v)
	}

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		routeReq(w, r, map_)
	})

	fmt.Printf("\nServer started on port %s...\n", PORT)
	http.ListenAndServe(":"+PORT, nil)
}

func routeReq(w http.ResponseWriter, r *http.Request, map_ map[string]string) {

	setCORSHeaders(r, w)

	uri := r.RequestURI
	if uri[0] == '/' {
		uri = uri[1:]
	}

	for k, v := range map_ {
		if strings.HasPrefix(uri, k) {
      dest := v + uri[len(k):]
			fmt.Printf("routing '%s' to '%s'\n", uri, dest)
			return
		}
	}

  fmt.Printf("failed routing '%s' (not found)\n", uri)
	w.WriteHeader(http.StatusNotFound)
}

func setCORSHeaders(r *http.Request, w http.ResponseWriter) {
	origin := r.Header.Get("Origin")
	if origin != "" {
		w.Header().Set("Access-Control-Allow-Origin", origin)
	}
	w.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
	w.Header().Set("Access-Control-Allow-Headers",
		"Accept, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")
}
