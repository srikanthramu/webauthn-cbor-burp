package burp.webauthncbor;

import org.junit.Test;
import burp.webauthncbor.Util;
import static org.junit.Assert.assertArrayEquals;

public class UtilTest {

    @Test
    public void testGetDecodedAttestObjectArrayNull() {
        byte[] errorArray = Util.getDecodedAttestObjectArray(null);
        assertArrayEquals(errorArray, Constants.ERROR_MSG.getBytes());
    }

    @Test
    public void testGetDecodedAttestObjectArrayEmpty() {
        byte[] errorArray = Util.getDecodedAttestObjectArray(new byte[]{});
        assertArrayEquals(errorArray, Constants.ERROR_MSG.getBytes());
    }
}
