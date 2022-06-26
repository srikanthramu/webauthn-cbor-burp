package burp;

// Java imports
import java.awt.Component;
import java.util.Base64;

// burp imports
import burp.IBurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IMessageEditorController;
import burp.IMessageEditorTab;
import burp.IMessageEditorTabFactory;
import burp.IParameter;

// Extension imports
import burp.ITextEditor;
import burp.webauthncbor.Constants;
import burp.webauthncbor.Util;
import burp.webauthncbor.Util;

/**
 * This Burp Extension supports decoding WebAuthn CBOR format. 
 * The implementation is based on https://github.com/PortSwigger/example-custom-editor-tab/tree/master/java
 * This implements IBurpExtender
 */
public class BurpExtender implements IBurpExtender, IMessageEditorTabFactory
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // keep a reference to our callbacks object
        this.callbacks = callbacks;
        
        // obtain an extension helpers object
        helpers = callbacks.getHelpers();
        
        // set our extension name
        callbacks.setExtensionName(Constants.EXTENSION_NAME);
        
        // register ourselves as a message editor tab factory
        callbacks.registerMessageEditorTabFactory(this);
    }

    //
    // implement IMessageEditorTabFactory
    //
    
    @Override
    public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable)
    {
    	// create a new instance of our custom editor tab
        return new WebAuthnCBORInputTab(controller, editable);
    }

    //
    // class implementing IMessageEditorTab
    //

    class WebAuthnCBORInputTab implements IMessageEditorTab
    {
        private boolean editable;
        private ITextEditor txtInput;
        private byte[] currentMessage;
        private String CBORPARAM = "attestationObject";
        
        public WebAuthnCBORInputTab(IMessageEditorController controller, boolean editable)
        {
        	// create an instance of Burp's text editor, to display our deserialized data
            txtInput = callbacks.createTextEditor();
            //callbacks.printOutput(txtInput.getClass().getName());
            txtInput.setEditable(editable);
        }

        //
        // implement IMessageEditorTab
        //

        @Override
        public String getTabCaption()
        {
            return "WebAuthn CBOR Decode";
        }

        @Override
        public Component getUiComponent()
        {
            return txtInput.getComponent();
        }

        @Override
        public boolean isEnabled(byte[] content, boolean isRequest)
        {
        	// enable this tab for requests containing a data parameter
            return isRequest && null != helpers.getRequestParameter(content, CBORPARAM);
        }

        @Override
        public void setMessage(byte[] content, boolean isRequest)
        {
        	if (content == null)
            {
                // clear our display
                txtInput.setText(null);
                txtInput.setEditable(false);
            }
            else
            {
                // retrieve the data parameter
                IParameter parameter = helpers.getRequestParameter(content, CBORPARAM);
                
                // deserialize the parameter value
                byte[] cborArray = Base64.getUrlDecoder().decode(parameter.getValue());
                byte[] decodedAttestObjArray = Util.getDecodedAttestObjectArray(cborArray);
                txtInput.setText(decodedAttestObjArray);
                txtInput.setEditable(editable);
            }
            
            // remember the displayed content
            currentMessage = content;
        }

        @Override
        public byte[] getMessage()
        {
        	// determine whether the user modified the deserialized data
            if (txtInput.isTextModified())
            {
                // reserialize the data
                byte[] text = txtInput.getText();
                //String input = helpers.urlEncode(helpers.base64Encode(text));
                String input = new String(text);
                // update the request with the new parameter value
                return helpers.updateParameter(currentMessage, helpers.buildParameter(CBORPARAM, input, IParameter.PARAM_BODY));
            }
            else return currentMessage;
        }

        @Override
        public boolean isModified()
        {
            return txtInput.isTextModified();
        }

        @Override
        public byte[] getSelectedData()
        {
            return txtInput.getSelectedText();
        }
    }

}