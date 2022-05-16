
package ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalTime;
import java.time.Duration;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.LocalTime;
import ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties.Vocabulary;
import ontology.generator.classes.examples.allRangeTypesOfDatatypeProperties.entities.Class1;

public class Class1Serialization extends SerializationModel<Class1>{

    @Override
    public void addToModel(Model model, Class1 class1) {
        model.add(class1.getIri(),RDF.TYPE, class1.getClassIRI());
        addPropertiesToModel(model,class1);

    }

    protected void addPropertiesToModel(Model model,  Class1 class1) {
        List<Object> hasLong2Pom = new ArrayList<>();
        hasLong2Pom.addAll(class1.getHasLong2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASLONG2_PROPERTY_IRI,hasLong2Pom);

        if(class1.getHasAnyURI() != null){
            model.add(class1.getIri(),Vocabulary.HASANYURI_PROPERTY_IRI,Values.literal(class1.getHasAnyURI()));
        }

        if(class1.getHasInteger() != null){
            model.add(class1.getIri(),Vocabulary.HASINTEGER_PROPERTY_IRI,Values.literal(class1.getHasInteger()));
        }

        if(class1.getHasLanguage() != null){
            model.add(class1.getIri(),Vocabulary.HASLANGUAGE_PROPERTY_IRI,Values.literal(class1.getHasLanguage()));
        }

        List<Object> hasDate2Pom = new ArrayList<>();
        hasDate2Pom.addAll(class1.getHasDate2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASDATE2_PROPERTY_IRI,hasDate2Pom);

        if(class1.getHasDouble() != null){
            model.add(class1.getIri(),Vocabulary.HASDOUBLE_PROPERTY_IRI,Values.literal(class1.getHasDouble()));
        }

        List<Object> hasDecimal2Pom = new ArrayList<>();
        hasDecimal2Pom.addAll(class1.getHasDecimal2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASDECIMAL2_PROPERTY_IRI,hasDecimal2Pom);

        List<Object> hasDouble2Pom = new ArrayList<>();
        hasDouble2Pom.addAll(class1.getHasDouble2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASDOUBLE2_PROPERTY_IRI,hasDouble2Pom);

        if(class1.getHasDateTime() != null){
            model.add(class1.getIri(),Vocabulary.HASDATETIME_PROPERTY_IRI,Values.literal(class1.getHasDateTime()));
        }

        List<Object> hasAnyURI2Pom = new ArrayList<>();
        hasAnyURI2Pom.addAll(class1.getHasAnyURI2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASANYURI2_PROPERTY_IRI,hasAnyURI2Pom);

        List<Object> hasByte2Pom = new ArrayList<>();
        hasByte2Pom.addAll(class1.getHasByte2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASBYTE2_PROPERTY_IRI,hasByte2Pom);

        List<Object> hasInt2Pom = new ArrayList<>();
        hasInt2Pom.addAll(class1.getHasInt2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASINT2_PROPERTY_IRI,hasInt2Pom);

        List<Object> hasBoolean2Pom = new ArrayList<>();
        hasBoolean2Pom.addAll(class1.getHasBoolean2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASBOOLEAN2_PROPERTY_IRI,hasBoolean2Pom);

        if(class1.getHasFloat() != null){
            model.add(class1.getIri(),Vocabulary.HASFLOAT_PROPERTY_IRI,Values.literal(class1.getHasFloat()));
        }

        List<Object> hasDayTimeDuration2Pom = new ArrayList<>();
        hasDayTimeDuration2Pom.addAll(class1.getHasDayTimeDuration2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASDAYTIMEDURATION2_PROPERTY_IRI,hasDayTimeDuration2Pom);

        if(class1.getHasInt() != null){
            model.add(class1.getIri(),Vocabulary.HASINT_PROPERTY_IRI,Values.literal(class1.getHasInt()));
        }

        if(class1.getHasTime() != null){
            model.add(class1.getIri(),Vocabulary.HASTIME_PROPERTY_IRI,Values.literal(class1.getHasTime()));
        }

        List<Object> hasShort2Pom = new ArrayList<>();
        hasShort2Pom.addAll(class1.getHasShort2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASSHORT2_PROPERTY_IRI,hasShort2Pom);

        if(class1.getHasNonNegativeInteger() != null){
            model.add(class1.getIri(),Vocabulary.HASNONNEGATIVEINTEGER_PROPERTY_IRI,Values.literal(class1.getHasNonNegativeInteger()));
        }

        if(class1.getHasDecimal() != null){
            model.add(class1.getIri(),Vocabulary.HASDECIMAL_PROPERTY_IRI,Values.literal(class1.getHasDecimal()));
        }

        List<Object> hasInteger2Pom = new ArrayList<>();
        hasInteger2Pom.addAll(class1.getHasInteger2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASINTEGER2_PROPERTY_IRI,hasInteger2Pom);

        if(class1.getHasBoolean() != null){
            model.add(class1.getIri(),Vocabulary.HASBOOLEAN_PROPERTY_IRI,Values.literal(class1.getHasBoolean()));
        }

        List<Object> hasNonNegativeInteger2Pom = new ArrayList<>();
        hasNonNegativeInteger2Pom.addAll(class1.getHasNonNegativeInteger2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASNONNEGATIVEINTEGER2_PROPERTY_IRI,hasNonNegativeInteger2Pom);

        List<Object> hasTime2Pom = new ArrayList<>();
        hasTime2Pom.addAll(class1.getHasTime2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASTIME2_PROPERTY_IRI,hasTime2Pom);

        List<Object> hasString2Pom = new ArrayList<>();
        hasString2Pom.addAll(class1.getHasString2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASSTRING2_PROPERTY_IRI,hasString2Pom);

        if(class1.getHasLong() != null){
            model.add(class1.getIri(),Vocabulary.HASLONG_PROPERTY_IRI,Values.literal(class1.getHasLong()));
        }

        if(class1.getHasShort() != null){
            model.add(class1.getIri(),Vocabulary.HASSHORT_PROPERTY_IRI,Values.literal(class1.getHasShort()));
        }

        if(class1.getHasString() != null){
            model.add(class1.getIri(),Vocabulary.HASSTRING_PROPERTY_IRI,Values.literal(class1.getHasString()));
        }

        List<Object> hasDateTimeStamp2Pom = new ArrayList<>();
        hasDateTimeStamp2Pom.addAll(class1.getHasDateTimeStamp2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASDATETIMESTAMP2_PROPERTY_IRI,hasDateTimeStamp2Pom);

        if(class1.getHasDayTimeDuration() != null){
            model.add(class1.getIri(),Vocabulary.HASDAYTIMEDURATION_PROPERTY_IRI,Values.literal(class1.getHasDayTimeDuration()));
        }

        if(class1.getHasByte() != null){
            model.add(class1.getIri(),Vocabulary.HASBYTE_PROPERTY_IRI,Values.literal(class1.getHasByte()));
        }

        List<Object> hasFloat2Pom = new ArrayList<>();
        hasFloat2Pom.addAll(class1.getHasFloat2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASFLOAT2_PROPERTY_IRI,hasFloat2Pom);

        if(class1.getHasDateTimeStamp() != null){
            model.add(class1.getIri(),Vocabulary.HASDATETIMESTAMP_PROPERTY_IRI,Values.literal(class1.getHasDateTimeStamp()));
        }

        if(class1.getHasDate() != null){
            model.add(class1.getIri(),Vocabulary.HASDATE_PROPERTY_IRI,Values.literal(class1.getHasDate()));
        }

        List<Object> hasDateTime2Pom = new ArrayList<>();
        hasDateTime2Pom.addAll(class1.getHasDateTime2());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASDATETIME2_PROPERTY_IRI,hasDateTime2Pom);


    }

    protected void setProperties(Model model,Class1 class1,int nestingLevel) throws Exception{
        Set<Value> hasLong2 = super.getAllObjects(model,Vocabulary.HASLONG2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasLong2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasLong2(literalValue.longValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasLong2(literalValue.longValue() );

                    }
                }
            }
        }

        Literal hasAnyURI = super.getFirstLiteralObject(model,Vocabulary.HASANYURI_PROPERTY_IRI,class1.getIri());
        if ( hasAnyURI != null ){
            class1.setHasAnyURI(new java.net.URL(hasAnyURI.stringValue()) );
        }
        Literal hasInteger = super.getFirstLiteralObject(model,Vocabulary.HASINTEGER_PROPERTY_IRI,class1.getIri());
        if ( hasInteger != null ){
            class1.setHasInteger(Integer.valueOf(hasInteger.intValue()) );
        }
        Literal hasLanguage = super.getFirstLiteralObject(model,Vocabulary.HASLANGUAGE_PROPERTY_IRI,class1.getIri());
        if ( hasLanguage != null ){
            class1.setHasLanguage(hasLanguage.stringValue() );
        }
        Set<Value> hasDate2 = super.getAllObjects(model,Vocabulary.HASDATE2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasDate2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasDate2(literalValue.calendarValue().toGregorianCalendar().getTime() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasDate2(literalValue.calendarValue().toGregorianCalendar().getTime() );

                    }
                }
            }
        }

        Literal hasDouble = super.getFirstLiteralObject(model,Vocabulary.HASDOUBLE_PROPERTY_IRI,class1.getIri());
        if ( hasDouble != null ){
            class1.setHasDouble(hasDouble.doubleValue() );
        }
        Set<Value> hasDecimal2 = super.getAllObjects(model,Vocabulary.HASDECIMAL2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasDecimal2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasDecimal2(literalValue.floatValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasDecimal2(literalValue.floatValue() );

                    }
                }
            }
        }

        Set<Value> hasDouble2 = super.getAllObjects(model,Vocabulary.HASDOUBLE2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasDouble2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasDouble2(literalValue.doubleValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasDouble2(literalValue.doubleValue() );

                    }
                }
            }
        }

        Literal hasDateTime = super.getFirstLiteralObject(model,Vocabulary.HASDATETIME_PROPERTY_IRI,class1.getIri());
        if ( hasDateTime != null ){
            class1.setHasDateTime(hasDateTime.calendarValue().toGregorianCalendar().getTime() );
        }
        Set<Value> hasAnyURI2 = super.getAllObjects(model,Vocabulary.HASANYURI2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasAnyURI2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasAnyURI2(new java.net.URL(literalValue.stringValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasAnyURI2(new java.net.URL(literalValue.stringValue()) );

                    }
                }
            }
        }

        Set<Value> hasByte2 = super.getAllObjects(model,Vocabulary.HASBYTE2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasByte2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasByte2(literalValue.byteValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasByte2(literalValue.byteValue() );

                    }
                }
            }
        }

        Set<Value> hasInt2 = super.getAllObjects(model,Vocabulary.HASINT2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasInt2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasInt2(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasInt2(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasBoolean2 = super.getAllObjects(model,Vocabulary.HASBOOLEAN2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasBoolean2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasBoolean2(literalValue.booleanValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasBoolean2(literalValue.booleanValue() );

                    }
                }
            }
        }

        Literal hasFloat = super.getFirstLiteralObject(model,Vocabulary.HASFLOAT_PROPERTY_IRI,class1.getIri());
        if ( hasFloat != null ){
            class1.setHasFloat(hasFloat.floatValue() );
        }
        Set<Value> hasDayTimeDuration2 = super.getAllObjects(model,Vocabulary.HASDAYTIMEDURATION2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasDayTimeDuration2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasDayTimeDuration2(Duration.parse(literalValue.stringValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasDayTimeDuration2(Duration.parse(literalValue.stringValue()) );

                    }
                }
            }
        }

        Literal hasInt = super.getFirstLiteralObject(model,Vocabulary.HASINT_PROPERTY_IRI,class1.getIri());
        if ( hasInt != null ){
            class1.setHasInt(Integer.valueOf(hasInt.intValue()) );
        }
        Literal hasTime = super.getFirstLiteralObject(model,Vocabulary.HASTIME_PROPERTY_IRI,class1.getIri());
        if ( hasTime != null ){
            class1.setHasTime(LocalTime.of(hasTime.calendarValue().getHour(),hasTime.calendarValue().getMinute(),hasTime.calendarValue().getSecond()) );
        }
        Set<Value> hasShort2 = super.getAllObjects(model,Vocabulary.HASSHORT2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasShort2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasShort2(literalValue.shortValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasShort2(literalValue.shortValue() );

                    }
                }
            }
        }

        Literal hasNonNegativeInteger = super.getFirstLiteralObject(model,Vocabulary.HASNONNEGATIVEINTEGER_PROPERTY_IRI,class1.getIri());
        if ( hasNonNegativeInteger != null ){
            class1.setHasNonNegativeInteger(Integer.valueOf(hasNonNegativeInteger.intValue()) );
        }
        Literal hasDecimal = super.getFirstLiteralObject(model,Vocabulary.HASDECIMAL_PROPERTY_IRI,class1.getIri());
        if ( hasDecimal != null ){
            class1.setHasDecimal(hasDecimal.floatValue() );
        }
        Set<Value> hasInteger2 = super.getAllObjects(model,Vocabulary.HASINTEGER2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasInteger2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasInteger2(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasInteger2(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Literal hasBoolean = super.getFirstLiteralObject(model,Vocabulary.HASBOOLEAN_PROPERTY_IRI,class1.getIri());
        if ( hasBoolean != null ){
            class1.setHasBoolean(hasBoolean.booleanValue() );
        }
        Set<Value> hasNonNegativeInteger2 = super.getAllObjects(model,Vocabulary.HASNONNEGATIVEINTEGER2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasNonNegativeInteger2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasNonNegativeInteger2(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasNonNegativeInteger2(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasTime2 = super.getAllObjects(model,Vocabulary.HASTIME2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasTime2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasTime2(LocalTime.of(literalValue.calendarValue().getHour(),literalValue.calendarValue().getMinute(),literalValue.calendarValue().getSecond()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasTime2(LocalTime.of(literalValue.calendarValue().getHour(),literalValue.calendarValue().getMinute(),literalValue.calendarValue().getSecond()) );

                    }
                }
            }
        }

        Set<Value> hasString2 = super.getAllObjects(model,Vocabulary.HASSTRING2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasString2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasString2(literalValue.stringValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasString2(literalValue.stringValue() );

                    }
                }
            }
        }

        Literal hasLong = super.getFirstLiteralObject(model,Vocabulary.HASLONG_PROPERTY_IRI,class1.getIri());
        if ( hasLong != null ){
            class1.setHasLong(hasLong.longValue() );
        }
        Literal hasShort = super.getFirstLiteralObject(model,Vocabulary.HASSHORT_PROPERTY_IRI,class1.getIri());
        if ( hasShort != null ){
            class1.setHasShort(hasShort.shortValue() );
        }
        Literal hasString = super.getFirstLiteralObject(model,Vocabulary.HASSTRING_PROPERTY_IRI,class1.getIri());
        if ( hasString != null ){
            class1.setHasString(hasString.stringValue() );
        }
        Set<Value> hasDateTimeStamp2 = super.getAllObjects(model,Vocabulary.HASDATETIMESTAMP2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasDateTimeStamp2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasDateTimeStamp2(new java.sql.Timestamp(literalValue.calendarValue().toGregorianCalendar().getTimeInMillis()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasDateTimeStamp2(new java.sql.Timestamp(literalValue.calendarValue().toGregorianCalendar().getTimeInMillis()) );

                    }
                }
            }
        }

        Literal hasDayTimeDuration = super.getFirstLiteralObject(model,Vocabulary.HASDAYTIMEDURATION_PROPERTY_IRI,class1.getIri());
        if ( hasDayTimeDuration != null ){
            class1.setHasDayTimeDuration(Duration.parse(hasDayTimeDuration.stringValue()) );
        }
        Literal hasByte = super.getFirstLiteralObject(model,Vocabulary.HASBYTE_PROPERTY_IRI,class1.getIri());
        if ( hasByte != null ){
            class1.setHasByte(hasByte.byteValue() );
        }
        Set<Value> hasFloat2 = super.getAllObjects(model,Vocabulary.HASFLOAT2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasFloat2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasFloat2(literalValue.floatValue() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasFloat2(literalValue.floatValue() );

                    }
                }
            }
        }

        Literal hasDateTimeStamp = super.getFirstLiteralObject(model,Vocabulary.HASDATETIMESTAMP_PROPERTY_IRI,class1.getIri());
        if ( hasDateTimeStamp != null ){
            class1.setHasDateTimeStamp(new java.sql.Timestamp(hasDateTimeStamp.calendarValue().toGregorianCalendar().getTimeInMillis()) );
        }
        Literal hasDate = super.getFirstLiteralObject(model,Vocabulary.HASDATE_PROPERTY_IRI,class1.getIri());
        if ( hasDate != null ){
            class1.setHasDate(hasDate.calendarValue().toGregorianCalendar().getTime() );
        }
        Set<Value> hasDateTime2 = super.getAllObjects(model,Vocabulary.HASDATETIME2_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasDateTime2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasDateTime2(literalValue.calendarValue().toGregorianCalendar().getTime() );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasDateTime2(literalValue.calendarValue().toGregorianCalendar().getTime() );

                    }
                }
            }
        }


    }

    @Override
    public Class1 getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Class1.CLASS_IRI);
        if(statements.size() != 0){
            Class1 class1 = new Class1(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, class1,nestingLevel);
            }
            return class1;
        }

        return null;
    }

    @Override
    public Collection<Class1> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Class1.CLASS_IRI);
        Collection<Class1> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Class1 class1 = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(class1);
            }
        }

        return allInstances;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        Set<Value> rdfCollections = model.filter(instanceIri,null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(instanceIri,null,(BNode)node);
        }
        model.remove(instanceIri,RDF.TYPE,Class1.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Class1 class1){
        Set<Value> rdfCollections = model.filter(class1.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(class1.getIri(),null,node);
        }

        Model statements = model.filter(class1.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,class1);
    }



}

