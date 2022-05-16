import ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties.RangeTypesOfDatatypePropertiesTestFactory;
import ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties.entities.Class1;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  AllRangeTypesTests.java
 *
 *  Unit tests for Generated source code.
 *  Source code was generated from allRangeTypes.owl ontology.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllRangeTypesTests {
    public Model model = new TreeModel();
    RangeTypesOfDatatypePropertiesTestFactory factory;

    String Class1InstanceName = "http://example.com/Class1Instance";

    String Class2InstanceName = "http://example.com/Class2Instance";

    String URLValue = "http://www.example.com/";

    // argument to run onto-code-maker-result-tester/src/main/resources/ontologies/allRangeTypesOfDatatypeProperties.owl -d onto-code-maker-result-tester/src/main/java/ontology/generator/classes/examples/allRangeTypesOfDatatypeProperties  -p ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties

    @BeforeEach
    void setUp() throws Exception {
        String[] inputFiles = {"src/main/resources/ontologies/allRangeTypes.owl"};
        OntologyParser ontoParser = new OntologyParser();
        model.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        factory = new RangeTypesOfDatatypePropertiesTestFactory(model);
    }


    @Test
    @Order(1)
    @DisplayName("Create instance of class")
    void testCreateInstance() {
        Class1 h = factory.createClass1(Class1InstanceName);
        assertEquals(Class1InstanceName, h.getIri().stringValue(), "Problem create Instance of Class1");
    }

    //functional properties

    @Test
    @Order(2)
    @DisplayName("Test Any URL property")
    void testAnyURLFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        URL urlVal = new URL(URLValue);
        c.setHasAnyURI(urlVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasAnyURI(),"URL value is not in the model.");
        assertEquals(urlVal,  loadedC.getHasAnyURI(),
                "URL values are not equal.");
    }

    @Test
    @Order(3)
    @DisplayName("Test Integer property")
    void testIntegerFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Integer integerVal = 10;
        c.setHasInteger(integerVal);

        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);

        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasInteger(),"Integer value is not in the model.");
        assertEquals(integerVal,  loadedC.getHasInteger(),
                "Integer values are not equal.");
    }

    @Test
    @Order(4)
    @DisplayName("Test Long property")
    void testLongFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        long longValue = 10L;
        c.setHasLong(longValue);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(longValue,  loadedC.getHasLong(), "Long values are not equal.");
    }

    @Test
    @Order(5)
    @DisplayName("Test Double property")
    void testDoubleFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        double doubleVal = 10D;
        c.setHasDouble(doubleVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(doubleVal,  loadedC.getHasDouble(),
                "Double values are not equal.");
    }

    @Test
    @Order(6)
    @DisplayName("Test Short property")
    void testShortFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        short shortVal = 10;
        c.setHasShort(shortVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(shortVal,  loadedC.getHasShort(),
                "Short values are not equal.");
    }

    @Test
    @Order(7)
    @DisplayName("Test DateTime property")
    void testDateTimeFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Date dateTimeVal = new Date();
        c.setHasDateTime(dateTimeVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasDateTime(),"DateTime value is not in the model.");
        assertEquals(dateTimeVal,  loadedC.getHasDateTime(),
                "DateTime values are not equal.");
    }

    @Test
    @Order(8)
    @DisplayName("Test DayTimeDuration property")
    void testDayTimeDurationFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Duration dayTimeDurationVal = Duration.ofSeconds(60);
        c.setHasDayTimeDuration(dayTimeDurationVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasDayTimeDuration(),"DayTimeDuration value is not in the model.");
        assertEquals(dayTimeDurationVal,  loadedC.getHasDayTimeDuration(),
                "DayTimeDuration values are not equal.");
    }

    @Test
    @Order(9)
    @DisplayName("Test Byte property")
    void testByteFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        byte byteVal = Byte.parseByte("10");
        c.setHasByte(byteVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(byteVal,  loadedC.getHasByte(),
                "Byte values are not equal.");
    }

    @Test
    @Order(10)
    @DisplayName("Test Float property")
    void testFloatFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        float floatVal = 1.5f;
        c.setHasFloat(floatVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(floatVal,  loadedC.getHasFloat(),
                "Float values are not equal.");
    }

    @Test
    @Order(11)
    @DisplayName("Test DateTimeStamp property")
    void testDateTimeStampFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        long time = new Date().getTime();
        Timestamp dateTimeStampVal = new Timestamp(time);
        c.setHasDateTimeStamp(dateTimeStampVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasDateTimeStamp(),"DateTimeStamp value is not in the model.");
        assertEquals(dateTimeStampVal,  loadedC.getHasDateTimeStamp(),
                "DateTimeStamp values are not equal.");
    }

    @Test
    @Order(12)
    @DisplayName("Test Date property")
    void testDateFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Date dateVal = new Date();
        c.setHasDate(dateVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasDate(),"Date value is not in the model.");
        assertEquals(dateVal,  loadedC.getHasDate(),
                "Date values are not equal.");
    }

    @Test
    @Order(13)
    @DisplayName("Test Int property")
    void testIntFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        int intVal = 17;
        c.setHasInt(intVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(intVal,  loadedC.getHasInt(),
                "Int values are not equal.");
    }

    @Test
    @Order(14)
    @DisplayName("Test Time property")
    void testTimeFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        LocalTime timeVal = LocalTime.ofSecondOfDay(100);
        c.setHasTime(timeVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getHasTime(),"Time value is not in the model.");
        assertEquals(timeVal,  loadedC.getHasTime(),
                "Time values are not equal.");
    }

    @Test
    @Order(15)
    @DisplayName("Test NonNegativeInteger property")
    void testNonNegativeIntegerFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        int nonNegativeIntegerVal = 100;
        c.setHasNonNegativeInteger(nonNegativeIntegerVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(nonNegativeIntegerVal,  loadedC.getHasNonNegativeInteger(),
                "NonNegativeInteger values are not equal.");
    }

    @Test
    @Order(16)
    @DisplayName("Test Decimal property")
    void testDecimalFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        float decimalVal = 14.447f;
        c.setHasDecimal(decimalVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(decimalVal,  loadedC.getHasDecimal(),
                "Decimal values are not equal.");
    }

    @Test
    @Order(17)
    @DisplayName("Test Boolean property")
    void testBooleanFunctionalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setHasBoolean(true);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertTrue( loadedC.getHasBoolean(),
                "Boolean value is not True.");
    }


    @Test
    @Order(18)
    @DisplayName("Test Any URL property")
    void testAnyURLProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        URL urlVal = new URL(URLValue);
        c.addHasAnyURI2(urlVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasAnyURI2().size(),"URL value is not in the collection.");
        assertEquals(urlVal,  loadedC.getHasAnyURI2().get(0),
                "URL values are not equal.");
    }

    @Test
    @Order(19)
    @DisplayName("Test Integer property")
    void testIntegerProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Integer integerVal = 10;
        c.addHasInteger2(integerVal);

        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);

        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasInteger2().size(),"Integer value is not in the collection.");
        assertEquals(integerVal,  loadedC.getHasInteger2().get(0),
                "Integer values are not equal.");
    }

    @Test
    @Order(20)
    @DisplayName("Test Long property")
    void testLongProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        long longValue = 10L;
        c.addHasLong2(longValue);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasLong2().size(),"Long value is not in the collection.");
        assertEquals(longValue,  loadedC.getHasLong2().get(0), "Long values are not equal.");
    }

    @Test
    @Order(21)
    @DisplayName("Test Double property")
    void testDoubleProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        double doubleVal = 10D;
        c.addHasDouble2(doubleVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasDouble2().size(),"Double value is not in the collection.");
        assertEquals(doubleVal,  loadedC.getHasDouble2().get(0),
                "Double values are not equal.");
    }

    @Test
    @Order(22)
    @DisplayName("Test Short property")
    void testShortProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        short shortVal = 10;
        c.addHasShort2(shortVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasShort2().size(),"Short value is not in the collection.");
        assertEquals(shortVal,  loadedC.getHasShort2().get(0),
                "Short values are not equal.");
    }

    @Test
    @Order(23)
    @DisplayName("Test DateTime property")
    void testDateTimeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Date dateTimeVal = new Date();
        c.addHasDateTime2(dateTimeVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasDateTime2().size(),"DateTime value is not in the collection.");
        assertEquals(dateTimeVal,  loadedC.getHasDateTime2().get(0),
                "DateTime values are not equal.");
    }

    @Test
    @Order(24)
    @DisplayName("Test DayTimeDuration property")
    void testDayTimeDurationProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Duration dayTimeDurationVal = Duration.ofSeconds(60);
        c.addHasDayTimeDuration2(dayTimeDurationVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasDayTimeDuration2().size(),"DayTimeDuration value is not in the collection.");
        assertEquals(dayTimeDurationVal,  loadedC.getHasDayTimeDuration2().get(0),
                "DayTimeDuration values are not equal.");
    }

    @Test
    @Order(25)
    @DisplayName("Test Byte property")
    void testByteProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        byte byteVal = Byte.parseByte("10");
        c.addHasByte2(byteVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertEquals(1,loadedC.getHasByte2().size(),"Byte value is not in the collection.");
        assertEquals(byteVal,  loadedC.getHasByte2().get(0),
                "Byte values are not equal.");
    }

    @Test
    @Order(26)
    @DisplayName("Test Float property")
    void testFloatProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        float floatVal = 1.5f;
        c.addHasFloat2(floatVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertEquals(1,loadedC.getHasFloat2().size(),"Float value is not in the collection.");
        assertEquals(floatVal,  loadedC.getHasFloat2().get(0),
                "Float values are not equal.");
    }

    @Test
    @Order(27)
    @DisplayName("Test DateTimeStamp property")
    void testDateTimeStampProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        long time = new Date().getTime();
        Timestamp dateTimeStampVal = new Timestamp(time);
        c.addHasDateTimeStamp2(dateTimeStampVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasDateTimeStamp2().size(),"DateTimeStamp value is not in the collection.");
        assertEquals(dateTimeStampVal,  loadedC.getHasDateTimeStamp2().get(0),
                "DateTimeStamp values are not equal.");
    }

    @Test
    @Order(28)
    @DisplayName("Test Date property")
    void testDateProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        Date dateVal = new Date();
        c.addHasDate2(dateVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasDate2().size(),"Int value is not in the collection.");
        assertEquals(dateVal,  loadedC.getHasDate2().get(0),
                "Date values are not equal.");
    }

    @Test
    @Order(29)
    @DisplayName("Test Int property")
    void testIntProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        int intVal = 17;
        c.addHasInt2(intVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasInt2().size(),"Int value is not in the collection.");
        assertEquals(intVal,  loadedC.getHasInt2().get(0),
                "Int values are not equal.");
    }

    @Test
    @Order(30)
    @DisplayName("Test Time property")
    void testTimeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        LocalTime timeVal = LocalTime.ofSecondOfDay(100);
        c.addHasTime2(timeVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasTime2().size(),"Time value is not in the collection.");
        assertEquals(timeVal,  loadedC.getHasTime2().get(0),
                "Time values are not equal.");
    }

    @Test
    @Order(31)
    @DisplayName("Test NonNegativeInteger property")
    void testNonNegativeIntegerProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        int nonNegativeIntegerVal = 100;
        c.addHasNonNegativeInteger2(nonNegativeIntegerVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasNonNegativeInteger2().size(),"NonNegativeInteger value is not in the collection.");
        assertEquals(nonNegativeIntegerVal,  loadedC.getHasNonNegativeInteger2().get(0),
                "NonNegativeInteger values are not equal.");
    }

    @Test
    @Order(32)
    @DisplayName("Test Decimal property")
    void testDecimalProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        float decimalVal = 14.447f;
        c.addHasDecimal2(decimalVal);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasDecimal2().size(),"Decimal value is not in the collection.");
        assertEquals(decimalVal,  loadedC.getHasDecimal2().get(0),
                "Decimal values are not equal.");
    }

    @Test
    @Order(33)
    @DisplayName("Test Boolean property")
    void testBooleanProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addHasBoolean2(true);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasBoolean2().size(),"Boolean value is not in the collection.");
        assertTrue( loadedC.getHasBoolean2().get(0), "Boolean value is not True.");
    }

    @Test
    @Order(34)
    @DisplayName("Test String property")
    void testStringProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        String testString = "Test";
        c.addHasString2(testString);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasString2().size(),"String value is not in the collection.");
        assertEquals(testString,  loadedC.getHasString2().get(0),
                "String values are not equal.");
    }

    @Test
    @Order(35)
    @DisplayName("Test String functional property")
    void testString2Property() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        String stringTest = "Test";
        c.setHasString(stringTest);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(stringTest,  loadedC.getHasString(),
                "String values are not equal.");
    }

    @Test
    @Order(36)
    @DisplayName("Test Language functional property")
    void testLanguageProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        String stringTest = "Language";
        c.setHasLanguage(stringTest);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(stringTest,  loadedC.getHasLanguage(),
                "Language values are not equal.");
    }

}
