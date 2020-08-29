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

func loadMap() []RouteMap {
	return []RouteMap{
		{"login", "http://localhost:6060/login"},
		{"signup", "http://localhost:6060/signup"},
		{"profile", "http://localhost:6060/profile"},
		{"tenants", "http://localhost:6060/tenants"},
		{"eventmeta", "http://localhost:6160/eventmeta"},
		{"stepmeta", "http://localhost:6160/stepmeta"},
		{"steps", "http://localhost:6160/steps"},
		{"events", "http://localhost:6160/events"},
		{"newsteps", "http://localhost:6160/newsteps"},
		{"workflows", "http://localhost:6160/workflows"},
		{"importleads", "http://localhost:6070/importleads"},
		{"imports", "http://localhost:6070/imports"},
		{"leads", "http://localhost:6160/leads"},
		{"", "http://localhost:6090/"},
	}
}

type RouteMap struct {
	pfx string
	pth string
}

func startServer(map_ []RouteMap) {
	const PORT = "80"
	fmt.Println("Gateway serving: ")
	for _, m := range map_ {
		fmt.Printf("  %s ==> %s\n", m.pfx, m.pth)
	}

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		routeReq(r, w, map_)
	})

	fmt.Printf("\nServer started on port %s...\n", PORT)
	http.ListenAndServe(":"+PORT, nil)
}

func routeReq(r *http.Request, w http.ResponseWriter, map_ []RouteMap) {

	uri := r.RequestURI
	if uri[0] == '/' {
		uri = uri[1:]
	}

	for _, m := range map_ {
		if strings.HasPrefix(uri, m.pfx) {
			dest := m.pth + uri[len(m.pfx):]
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

	if r.Method == "OPTIONS" {
		setCORSHeaders(r, w)
		return
	}

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

	hasCORS := false

	for k, val := range resp.Header {
		for _, v := range val {
			if strings.HasPrefix(k, "Access-Control-") {
				hasCORS = true
			}
			w.Header().Add(k, v)
		}
	}

	if !hasCORS {
		setCORSHeaders(r, w)
	}

	w.WriteHeader(resp.StatusCode)
	io.Copy(w, resp.Body)
	resp.Body.Close()
}
