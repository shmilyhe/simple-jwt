package io.shmilyhe.utils.encryption;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum JWTAlg {
    /**
     * JWA name for {@code No digital signature or MAC performed}
     */
    NONE("none", "No digital signature or MAC performed", "None", null, false, 0, 0),

    /**
     * JWA algorithm name for {@code HMAC using SHA-256}
     */
    HS256("HS256", "HMAC using SHA-256", "HMAC", "HmacSHA256", true, 256, 256, "1.2.840.113549.2.9"),

    /**
     * JWA algorithm name for {@code HMAC using SHA-384}
     */
    HS384("HS384", "HMAC using SHA-384", "HMAC", "HmacSHA384", true, 384, 384, "1.2.840.113549.2.10"),

    /**
     * JWA algorithm name for {@code HMAC using SHA-512}
     */
    HS512("HS512", "HMAC using SHA-512", "HMAC", "HmacSHA512", true, 512, 512, "1.2.840.113549.2.11"),

    /**
     * JWA algorithm name for {@code RSASSA-PKCS-v1_5 using SHA-256}
     */
    RS256("RS256", "RSASSA-PKCS-v1_5 using SHA-256", "RSA", "SHA256withRSA", true, 256, 2048),

    /**
     * JWA algorithm name for {@code RSASSA-PKCS-v1_5 using SHA-384}
     */
    RS384("RS384", "RSASSA-PKCS-v1_5 using SHA-384", "RSA", "SHA384withRSA", true, 384, 2048),

    /**
     * JWA algorithm name for {@code RSASSA-PKCS-v1_5 using SHA-512}
     */
    RS512("RS512", "RSASSA-PKCS-v1_5 using SHA-512", "RSA", "SHA512withRSA", true, 512, 2048),

    /**
     * JWA algorithm name for {@code ECDSA using P-256 and SHA-256}
     */
    ES256("ES256", "ECDSA using P-256 and SHA-256", "ECDSA", "SHA256withECDSA", true, 256, 256),

    /**
     * JWA algorithm name for {@code ECDSA using P-384 and SHA-384}
     */
    ES384("ES384", "ECDSA using P-384 and SHA-384", "ECDSA", "SHA384withECDSA", true, 384, 384),

    /**
     * JWA algorithm name for {@code ECDSA using P-521 and SHA-512}
     */
    ES512("ES512", "ECDSA using P-521 and SHA-512", "ECDSA", "SHA512withECDSA", true, 512, 521),

    /**
     * JWA algorithm name for {@code RSASSA-PSS using SHA-256 and MGF1 with SHA-256}.  <b>This algorithm requires
     * Java 11 or later or a JCA provider like BouncyCastle to be in the runtime classpath.</b>  If on Java 10 or
     * earlier, BouncyCastle will be used automatically if found in the runtime classpath.
     */
    PS256("PS256", "RSASSA-PSS using SHA-256 and MGF1 with SHA-256", "RSA", "RSASSA-PSS", false, 256, 2048),

    /**
     * JWA algorithm name for {@code RSASSA-PSS using SHA-384 and MGF1 with SHA-384}.  <b>This algorithm requires
     * Java 11 or later or a JCA provider like BouncyCastle to be in the runtime classpath.</b>  If on Java 10 or
     * earlier, BouncyCastle will be used automatically if found in the runtime classpath.
     */
    PS384("PS384", "RSASSA-PSS using SHA-384 and MGF1 with SHA-384", "RSA", "RSASSA-PSS", false, 384, 2048),

    /**
     * JWA algorithm name for {@code RSASSA-PSS using SHA-512 and MGF1 with SHA-512}. <b>This algorithm requires
     * Java 11 or later or a JCA provider like BouncyCastle to be in the runtime classpath.</b>  If on Java 10 or
     * earlier, BouncyCastle will be used automatically if found in the runtime classpath.
     */
    PS512("PS512", "RSASSA-PSS using SHA-512 and MGF1 with SHA-512", "RSA", "RSASSA-PSS", false, 512, 2048);

    //purposefully ordered higher to lower:
    private static final List<JWTAlg> PREFERRED_HMAC_ALGS = Collections.unmodifiableList(Arrays.asList(
        JWTAlg.HS512, JWTAlg.HS384, JWTAlg.HS256));
    //purposefully ordered higher to lower:
    private static final List<JWTAlg> PREFERRED_EC_ALGS = Collections.unmodifiableList(Arrays.asList(
        JWTAlg.ES512, JWTAlg.ES384, JWTAlg.ES256));

    private final String value;
    private final String description;
    private final String familyName;
    private final String jcaName;
    private final boolean jdkStandard;
    private final int digestLength;
    private final int minKeyLength;
    /**
     * Algorithm name as given by {@link Key#getAlgorithm()} if the key was loaded from a pkcs12 Keystore.
     *
     * @deprecated This is just a workaround for https://bugs.openjdk.java.net/browse/JDK-8243551
     */
    @Deprecated
    private final String pkcs12Name;

    JWTAlg(String value, String description, String familyName, String jcaName, boolean jdkStandard,
                       int digestLength, int minKeyLength) {
        this(value, description,familyName, jcaName, jdkStandard, digestLength, minKeyLength, jcaName);
    }

    JWTAlg(String value, String description, String familyName, String jcaName, boolean jdkStandard,
                       int digestLength, int minKeyLength, String pkcs12Name) {
        this.value = value;
        this.description = description;
        this.familyName = familyName;
        this.jcaName = jcaName;
        this.jdkStandard = jdkStandard;
        this.digestLength = digestLength;
        this.minKeyLength = minKeyLength;
        this.pkcs12Name = pkcs12Name;
    }

    /**
     * Returns the JWA algorithm name constant.
     *
     * @return the JWA algorithm name constant.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the JWA algorithm description.
     *
     * @return the JWA algorithm description.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Returns the cryptographic family name of the signature algorithm.  The value returned is according to the
     * following table:
     *
     * <table>
     * <caption>Crypto Family</caption>
     * <thead>
     * <tr>
     * <th>SignatureAlgorithm</th>
     * <th>Family Name</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>HS256</td>
     * <td>HMAC</td>
     * </tr>
     * <tr>
     * <td>HS384</td>
     * <td>HMAC</td>
     * </tr>
     * <tr>
     * <td>HS512</td>
     * <td>HMAC</td>
     * </tr>
     * <tr>
     * <td>RS256</td>
     * <td>RSA</td>
     * </tr>
     * <tr>
     * <td>RS384</td>
     * <td>RSA</td>
     * </tr>
     * <tr>
     * <td>RS512</td>
     * <td>RSA</td>
     * </tr>
     * <tr>
     * <td>PS256</td>
     * <td>RSA</td>
     * </tr>
     * <tr>
     * <td>PS384</td>
     * <td>RSA</td>
     * </tr>
     * <tr>
     * <td>PS512</td>
     * <td>RSA</td>
     * </tr>
     * <tr>
     * <td>ES256</td>
     * <td>ECDSA</td>
     * </tr>
     * <tr>
     * <td>ES384</td>
     * <td>ECDSA</td>
     * </tr>
     * <tr>
     * <td>ES512</td>
     * <td>ECDSA</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @return Returns the cryptographic family name of the signature algorithm.
     * @since 0.5
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Returns the name of the JCA algorithm used to compute the signature.
     *
     * @return the name of the JCA algorithm used to compute the signature.
     */
    public String getJcaName() {
        return jcaName;
    }

    /**
     * Returns {@code true} if the algorithm is supported by standard JDK distributions or {@code false} if the
     * algorithm implementation is not in the JDK and must be provided by a separate runtime JCA Provider (like
     * BouncyCastle for example).
     *
     * @return {@code true} if the algorithm is supported by standard JDK distributions or {@code false} if the
     * algorithm implementation is not in the JDK and must be provided by a separate runtime JCA Provider (like
     * BouncyCastle for example).
     */
    public boolean isJdkStandard() {
        return jdkStandard;
    }

    /**
     * Returns {@code true} if the enum instance represents an HMAC signature algorithm, {@code false} otherwise.
     *
     * @return {@code true} if the enum instance represents an HMAC signature algorithm, {@code false} otherwise.
     */
    public boolean isHmac() {
        return familyName.equals("HMAC");
    }

    /**
     * Returns {@code true} if the enum instance represents an RSA public/private key pair signature algorithm,
     * {@code false} otherwise.
     *
     * @return {@code true} if the enum instance represents an RSA public/private key pair signature algorithm,
     * {@code false} otherwise.
     */
    public boolean isRsa() {
        return familyName.equals("RSA");
    }

    /**
     * Returns {@code true} if the enum instance represents an Elliptic Curve ECDSA signature algorithm, {@code false}
     * otherwise.
     *
     * @return {@code true} if the enum instance represents an Elliptic Curve ECDSA signature algorithm, {@code false}
     * otherwise.
     */
    public boolean isEllipticCurve() {
        return familyName.equals("ECDSA");
    }

    /**
     * Returns the minimum key length in bits (not bytes) that may be used with this algorithm according to the
     * <a href="https://tools.ietf.org/html/rfc7518">JWT JWA Specification (RFC 7518)</a>.
     *
     * @return the minimum key length in bits (not bytes) that may be used with this algorithm according to the
     * <a href="https://tools.ietf.org/html/rfc7518">JWT JWA Specification (RFC 7518)</a>.
     * @since 0.10.0
     */
    public int getMinKeyLength() {
        return this.minKeyLength;
    }
}
