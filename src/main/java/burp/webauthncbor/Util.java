package burp.webauthncbor;

// other dependency imports
import com.webauthn4j.converter.util.CborConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.converter.util.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.databind.SerializationFeature;

// Burp imports
import burp.IExtensionHelpers;
import burp.IParameter;
import burp.ITextEditor;

/**
 * A collection of utilities
 */
public class Util {
    
    /**
     * Decodes the attestation object in CBOR format
     * @param cborArray
     * @return
     */
    public static byte[] getDecodedAttestObjectArray(byte[] cborArray) {
        // Check if the attestationObject value is empty
        if (cborArray != null && cborArray.length > 0) {
            // Ensure the out is propery indented
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            ObjectConverter objectConverter = new ObjectConverter(objectMapper, new ObjectMapper(new CBORFactory()));
            CborConverter cborConverter = objectConverter.getCborConverter();
            // Decode the CBOR using a library
            AttestationObject attestObj = cborConverter.readValue(cborArray, AttestationObject.class);        
            JsonConverter jsonConverter = objectConverter.getJsonConverter();
            byte[] decodedAttestObjArray = jsonConverter.writeValueAsString(attestObj).getBytes();
            return decodedAttestObjArray;
        }
        // Return error message when valid attestationObject CBOR data is not found
        return Constants.ERROR_MSG.getBytes();
    }

    /**
     * Checks whether the content/webauthn request has CBOR data
     * @param helpers
     * @param content
     * @return
     */
    public static boolean isWebAuthnCBOR(IExtensionHelpers helpers, byte[] content) {
        // To_DO: There could be CBOR data in other params
        return null != helpers.getRequestParameter(content, Constants.CBORPARAM);
    }

    /**
     * Returns the parameter that contains CBOR data
     * @param helpers
     * @param content
     * @return
     */
    public static IParameter getWebAuthnCBOR(IExtensionHelpers helpers, byte[] content) {
        // To_DO: There could be CBOR data in other params
        return helpers.getRequestParameter(content, Constants.CBORPARAM);
    }
    
    /**
     * Clears the editor display and makes it uneditable
     * @param txtInput
     */
    public static void clearEditorDisplay(ITextEditor txtInput) {
        txtInput.setText(null);
        txtInput.setEditable(false);
    }

}
