/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.integrations.orderprocessing.kafka.schema;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class PartnerDetails extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7314510285731368239L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PartnerDetails\",\"namespace\":\"com.integrations.orderprocessing.kafka.schema\",\"fields\":[{\"name\":\"parcelType\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PartnerDetails> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PartnerDetails> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<PartnerDetails> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<PartnerDetails> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<PartnerDetails> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this PartnerDetails to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a PartnerDetails from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a PartnerDetails instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static PartnerDetails fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.String parcelType;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PartnerDetails() {}

  /**
   * All-args constructor.
   * @param parcelType The new value for parcelType
   */
  public PartnerDetails(java.lang.String parcelType) {
    this.parcelType = parcelType;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return parcelType;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: parcelType = value$ != null ? value$.toString() : null; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'parcelType' field.
   * @return The value of the 'parcelType' field.
   */
  public java.lang.String getParcelType() {
    return parcelType;
  }


  /**
   * Sets the value of the 'parcelType' field.
   * @param value the value to set.
   */
  public void setParcelType(java.lang.String value) {
    this.parcelType = value;
  }

  /**
   * Creates a new PartnerDetails RecordBuilder.
   * @return A new PartnerDetails RecordBuilder
   */
  public static com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder newBuilder() {
    return new com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder();
  }

  /**
   * Creates a new PartnerDetails RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PartnerDetails RecordBuilder
   */
  public static com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder newBuilder(com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder other) {
    if (other == null) {
      return new com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder();
    } else {
      return new com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder(other);
    }
  }

  /**
   * Creates a new PartnerDetails RecordBuilder by copying an existing PartnerDetails instance.
   * @param other The existing instance to copy.
   * @return A new PartnerDetails RecordBuilder
   */
  public static com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder newBuilder(com.integrations.orderprocessing.kafka.schema.PartnerDetails other) {
    if (other == null) {
      return new com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder();
    } else {
      return new com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder(other);
    }
  }

  /**
   * RecordBuilder for PartnerDetails instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PartnerDetails>
    implements org.apache.avro.data.RecordBuilder<PartnerDetails> {

    private java.lang.String parcelType;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.parcelType)) {
        this.parcelType = data().deepCopy(fields()[0].schema(), other.parcelType);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing PartnerDetails instance
     * @param other The existing instance to copy.
     */
    private Builder(com.integrations.orderprocessing.kafka.schema.PartnerDetails other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.parcelType)) {
        this.parcelType = data().deepCopy(fields()[0].schema(), other.parcelType);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'parcelType' field.
      * @return The value.
      */
    public java.lang.String getParcelType() {
      return parcelType;
    }


    /**
      * Sets the value of the 'parcelType' field.
      * @param value The value of 'parcelType'.
      * @return This builder.
      */
    public com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder setParcelType(java.lang.String value) {
      validate(fields()[0], value);
      this.parcelType = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'parcelType' field has been set.
      * @return True if the 'parcelType' field has been set, false otherwise.
      */
    public boolean hasParcelType() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'parcelType' field.
      * @return This builder.
      */
    public com.integrations.orderprocessing.kafka.schema.PartnerDetails.Builder clearParcelType() {
      parcelType = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PartnerDetails build() {
      try {
        PartnerDetails record = new PartnerDetails();
        record.parcelType = fieldSetFlags()[0] ? this.parcelType : (java.lang.String) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PartnerDetails>
    WRITER$ = (org.apache.avro.io.DatumWriter<PartnerDetails>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PartnerDetails>
    READER$ = (org.apache.avro.io.DatumReader<PartnerDetails>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.parcelType);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.parcelType = in.readString();

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.parcelType = in.readString();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










