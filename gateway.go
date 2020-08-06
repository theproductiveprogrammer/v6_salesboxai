package main

import (
	"fmt"
	"io"
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
		routeReq(r, w, map_)
	})

	fmt.Printf("\nServer started on port %s...\n", PORT)
	http.ListenAndServe(":"+PORT, nil)
}

func routeReq(r *http.Request, w http.ResponseWriter, map_ map[string]string) {

	setCORSHeaders(r, w)

	uri := r.RequestURI
	if uri[0] == '/' {
		uri = uri[1:]
	}

	for k, v := range map_ {
		if strings.HasPrefix(uri, k) {
			dest := v + uri[len(k):]
			fmt.Printf("routing '%s' to '%s'\n", uri, dest)
			routeTo(dest, r, w)
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

/*    outcome/
 * We create a new request to the URI provided with the request
 * method, headers, and body then we copy across the response
 * body and headers.
 */
func routeTo(uri string, r *http.Request, w http.ResponseWriter) {
	var req *http.Request
	var resp *http.Response
	var err error

	defer r.Body.Close()

	req, err = http.NewRequest(r.Method, uri, r.Body)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	for name, value := range r.Header {
		for _, v := range value {
			req.Header.Add(name, v)
		}
	}
	client := &http.Client{}
	resp, err = client.Do(req)
	if err != nil {
		w.WriteHeader(500)
		return
	}

	for k, val := range resp.Header {
		for _, v := range val {
			w.Header().Add(k, v)
		}
	}
	w.WriteHeader(resp.StatusCode)
	io.Copy(w, resp.Body)
	resp.Body.Close()
}
