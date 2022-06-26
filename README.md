# WebAuthn CBOR Burp
WebAuthn CBOR is a Web Burp Extension to Decode WebAuthn CBOR format. [WebAuthn](https://www.w3.org/TR/webauthn-2/) is a W3C Standard to support strong authentication of users. WebAuthn depends on several other specifications such as Base64url encoding, Concise Binary Object Representation (CBOR), CBOR Object Signing and Encryption (COSE), for more details refer [3. Dependencies](https://www.w3.org/TR/webauthn-2/#sctn-dependencies). This extension enables to view the decoded [CBOR format](https://www.rfc-editor.org/info/rfc8949).

# Installation
1. Clone or download this repo.
2. Compile the code: `gradle bigJar`. The compiled jar location is `build/libs/webauthn-cbor-burp-all-1.0.jar`
3. [Follow the instructions](https://portswigger.net/burp/documentation/desktop/tools/extender#installing-an-extension-from-a-file) to load the jar.

# Usage
* Intercept the WebAuthn request/response using Burp proxy
* If the request has `attestationObject`, a new Tab `WebAuthn CBOR Decode` will be added and the decoded CBOR format will be dispalyed. See **Screeshot** security below.

## Screenshot
![](images/webauthn-cbor-decode.png)

## References
* https://fidoalliance.org/fido2-2/fido2-web-authentication-webauthn/
* https://webauthn.guide/
* https://webauthn.io/
* https://cbor.io/