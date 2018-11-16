package it.usi.xframe.xas.bfutil.data;

public class ENC_TYPE implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ENC_TYPE(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _ENCRYPT_SEG = "ENCRYPT_SEG";
    public static final java.lang.String _NO_ENCRYPTION = "NO_ENCRYPTION";
    public static final ENC_TYPE ENCRYPT_SEG = new ENC_TYPE(_ENCRYPT_SEG);
    public static final ENC_TYPE NO_ENCRYPTION = new ENC_TYPE(_NO_ENCRYPTION);
    public java.lang.String getValue() { return _value_;}
    public static ENC_TYPE fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        ENC_TYPE enumeration = (ENC_TYPE)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalStateException();
        return enumeration;
    }
    public static ENC_TYPE fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public static java.lang.String getAllowedValues() { return _table_.keySet().toString(); }
}
