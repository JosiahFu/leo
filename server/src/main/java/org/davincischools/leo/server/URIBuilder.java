package org.davincischools.leo.server;

import java.net.URI;
import java.net.URISyntaxException;

/* Copies a URI then allows modifications. */
public final class URIBuilder {

  // The following variables are copies of the fields that java.net.URI uses
  // to represent a URI.

  // Components of all URIs: [<scheme>:]<scheme-specific-part>[#<fragment>]
  private String scheme; // null ==> relative URI
  private String fragment;

  // The authority is intentionally excluded.
  //
  // Hierarchical URI components: [//<authority>]<path>[?<query>]
  // private String authority;         // Registry or server

  // Server-based authority: [<userInfo>@]<host>[:<port>]
  private String userInfo;
  private String host; // null ==> registry-based
  private int port = -1; // -1 ==> undefined

  // Remaining components of hierarchical URIs
  private String path; // null ==> opaque
  private String query;

  // Use the static methods.
  private URIBuilder() {}

  public static URIBuilder fromUri(URI uri) {
    URIBuilder uriBuilder = new URIBuilder();
    uriBuilder.scheme = uri.getScheme();
    uriBuilder.fragment = uri.getRawFragment();
    uriBuilder.userInfo = uri.getRawUserInfo();
    uriBuilder.host = uri.getHost();
    uriBuilder.port = uri.getPort();
    uriBuilder.path = uri.getRawPath();
    uriBuilder.query = uri.getRawQuery();
    return uriBuilder;
  }

  public URI build() throws URISyntaxException {
    return new URI(scheme, userInfo, host, port, path, query, fragment);
  }

  public String getScheme() {
    return scheme;
  }

  public URIBuilder setScheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  public String getFragment() {
    return fragment;
  }

  public URIBuilder setFragment(String fragment) {
    this.fragment = fragment;
    return this;
  }

  public String getUserInfo() {
    return userInfo;
  }

  public URIBuilder setUserInfo(String userInfo) {
    this.userInfo = userInfo;
    return this;
  }

  public String getHost() {
    return host;
  }

  public URIBuilder setHost(String host) {
    this.host = host;
    return this;
  }

  public int getPort() {
    return port;
  }

  public URIBuilder setPort(int port) {
    this.port = port;
    return this;
  }

  public String getPath() {
    return path;
  }

  public URIBuilder setPath(String path) {
    this.path = path;
    return this;
  }

  public String getQuery() {
    return query;
  }

  public URIBuilder setQuery(String query) {
    this.query = query;
    return this;
  }
}
