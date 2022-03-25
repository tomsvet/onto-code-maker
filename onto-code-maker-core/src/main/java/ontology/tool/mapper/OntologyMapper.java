package ontology.tool.mapper;

import ontology.tool.generator.representations.*;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.*;

import java.util.*;
import java.util.stream.Collectors;


public class OntologyMapper {
    private ModelManager modelManager;

    //List<ClassRepresentation> mappedClasses;
    List<IRI> PRIORVERSION_IRIS = new ArrayList<>(Collections.singletonList(OWL.PRIORVERSION));
    List<IRI> IMPORTS_IRIS = new ArrayList<>(Collections.singletonList(OWL.IMPORTS));
    List<IRI> CLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.CLASS,OWL.CLASS));
    List<IRI> COMMENT_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.COMMENT, DCTERMS.DESCRIPTION,
            SKOS.DEFINITION, DC.DESCRIPTION));
    List<IRI> LABEL_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.LABEL, DCTERMS.TITLE, DC.TITLE, SKOS.PREF_LABEL, SKOS.ALT_LABEL,SKOS.HIDDEN_LABEL));
    List<IRI> CREATOR_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(DC.CREATOR,DCTERMS.CREATOR));
    List<IRI> SUBCLASS_PREDICATE_IRIS = new ArrayList<>(Collections.singletonList(RDFS.SUBCLASSOF));
    List<IRI> EQUIVALENT_CLASS_PREDICATE_IRIS = new ArrayList<>(Collections.singletonList(OWL.EQUIVALENTCLASS));
    IRI SUBPROPERTY_PREDICATE_IRIS = RDFS.SUBPROPERTYOF;

    List<ClassRepresentation> ontologyClasses = new ArrayList<>();
    //List<AbstractClassRepresentation> abstractClassesMapping = new ArrayList<>();
    List<EquivalentClassRepresentation> equivalentClassesMapping = new ArrayList<>();



    public OntologyMapper(Model model){
        this.modelManager = new ModelManager(model);
    }

    public List<ClassRepresentation> getMappedClasses(){
        return ontologyClasses;
    }

    /*public List<AbstractClassRepresentation> getMappedAbstractClasses(){
        return abstractClassesMapping;
    }

    public List<NormalClassRepresentation> getAllMappedClasses(){
        List<NormalClassRepresentation> allClasses = new ArrayList<>();
        allClasses.addAll(ontologyClasses);
        allClasses.addAll(abstractClassesMapping);
        return allClasses;
    }*/

    public ClassRepresentation getMappedClass(Resource classResource){
        Optional<ClassRepresentation> res = ontologyClasses.stream().filter(classRep -> classRep.getResourceValue().equals(classResource)).findFirst();
        return res.orElse(null);
    }

    /*public AbstractClassRepresentation getMappedAbstractClassesMapping(Resource classId){
        Optional<AbstractClassRepresentation> res = abstractClassesMapping.stream().filter(classRep -> classRep.getResourceValue().equals(classId)).findFirst();
        return res.orElse(null);
    }*/

    public boolean isMappedClass(Value classValue){
        Optional<ClassRepresentation> res = ontologyClasses.stream().filter(classRep -> classRep.getResourceValue().equals(classValue)).findFirst();
        return res.isPresent();
    }

    public void mapping() throws Exception {

        mapClasses();

        // first need to map class equivalence and then hierarchy
        for(ClassRepresentation classRep:ontologyClasses){
            mapUnionOfClasses(classRep);
            mapEquivalentClasses(classRep);
            mapClassHierarchy(classRep);
            mapIntersectionOfClass(classRep);

        }


        // then can map class properties
        for(ClassRepresentation classRep:ontologyClasses){
            mapComplementOfClass(classRep);
            mapComments(classRep);
            mapLabels(classRep);
            mapCreator(classRep);
            mapProperties(classRep);
        }
    }

    public void mapClasses(){
        List<ClassRepresentation> allClassRep = new ArrayList<>();
        //List<AbstractClassRepresentation> allAbstractClassRep = new ArrayList<>();
        Set<Resource> classes = getClasses();
        for(Resource classResource:classes){
            if(classResource.isIRI()){
                IRI classIRI = (IRI) classResource;
                NormalClassRepresentation classRep = new NormalClassRepresentation(classIRI.getNamespace(),classIRI.getLocalName());
                allClassRep.add(classRep);
            }else if(classResource.isBNode()){
                BNode classBNode = (BNode) classResource;
                AbstractClassRepresentation abstractClassRep = new AbstractClassRepresentation(classBNode.getID());
                allClassRep.add(abstractClassRep);
            }
        }
        ontologyClasses = allClassRep;
        //abstractClassesMapping = allAbstractClassRep;
    }

    public Set<Resource> getClasses(){
        Set<Resource> allClasses = new HashSet<>();

        for(IRI classPredIRI:CLASS_PREDICATE_IRIS){
            Set<Resource> classes = modelManager.getAllSubjects(RDF.TYPE,classPredIRI);
            allClasses.addAll(classes);
        }
        return allClasses;
    }

    /*public void mapAbstractClassHierarchy(AbstractClassRepresentation classRep){
        Set<Value> superClasses = modelManager.getAllObjects(SUBCLASS_PREDICATE_IRIS,classRep.getResourceValue());
        for(Value superClassValue: superClasses) {
            if (superClassValue.isIRI()) {

            }else if(superClassValue.isBNode()){

            }
        }
        //todo
    }*/

    public void mapClassHierarchy(ClassRepresentation classRep) throws Exception {

        Set<Value> superClasses = modelManager.getAllObjects(SUBCLASS_PREDICATE_IRIS,classRep.getResourceValue());
        Set<Value> finalSuperList = new HashSet<>(superClasses);
        //to find subclass collections
        for(Value superClass: superClasses){
            if(superClass.isBNode()){
                List<Value> subClassOfClasses = modelManager.getRDFCollection((Resource)superClass);
                finalSuperList.remove(superClass);
                finalSuperList.addAll(subClassOfClasses);
            }
        }
        for(Value superClassValue: finalSuperList){
            if(superClassValue.isResource()) {
                Resource superClassResource = (Resource) superClassValue;
                ClassRepresentation superClassRep = getMappedClass(superClassResource);

                if (superClassRep != null) {
                    classRep.addSuperClasses(superClassRep);
                    superClassRep.addSubClasses(classRep);
                    if (classRep.getEquivalentClass() != null && !classRep.getEquivalentClass().getSuperClasses().contains(superClassRep)) {
                        classRep.getEquivalentClass().addSuperClasses(superClassRep);
                    }
                } else {
                    //todo test this error
                    throw new Exception("Class " + superClassResource.stringValue() + " doesn't exist in the ontology.");
                }
            }
        }
    }

    public Set<Value> mergeClassRepresenationLists(Set<Value> one,Set<Resource> two){
        Set<Value> finalList = new HashSet<>(one);
        for (Value x : two){
            if (!finalList.contains(x)){
                finalList.add(x);
            }
        }
        return finalList;
    }

    public List<ClassRepresentation> getEquivalentClasses(ClassRepresentation classRep, List<ClassRepresentation> actualEquivalentClasses) throws Exception {

        Set<Value> equivalentClasses = modelManager.getAllObjects(EQUIVALENT_CLASS_PREDICATE_IRIS,classRep.getResourceValue());
        Set<Resource> equivalentClassesSubjects = modelManager.getAllSubjects(EQUIVALENT_CLASS_PREDICATE_IRIS,classRep.getResourceValue());
        equivalentClasses.addAll(equivalentClassesSubjects);
        Set<Value> finalEqList = new HashSet<>(equivalentClasses);
        //to find equivalent collections
        for(Value eqClass: equivalentClasses){
            if(eqClass.isBNode()){
                List<Value> equivalentOfClasses = modelManager.getRDFCollection((Resource)eqClass);
                finalEqList.remove(eqClass);
                finalEqList.addAll(equivalentOfClasses);
            }
        }

        for(Value eqClass: finalEqList){

            if(eqClass.isResource()) {
                ClassRepresentation eqClassRep = getMappedClass((Resource) eqClass);

                if (eqClassRep != null) {
                    if (!actualEquivalentClasses.contains(eqClassRep)) {
                        actualEquivalentClasses.add(eqClassRep);
                        getEquivalentClasses(eqClassRep, actualEquivalentClasses);
                    }
                }else{
                    throw new Exception("Class " + eqClass.stringValue() + " doesn't exist in the ontology.");
                }
            }
        }
        return actualEquivalentClasses;
    }


    public void mapEquivalentClasses(ClassRepresentation classRep) throws Exception {
        Optional<EquivalentClassRepresentation> ret = equivalentClassesMapping.parallelStream().filter(eq -> eq.getEquivalentClasses().contains(classRep)).findFirst();
        if(ret.isEmpty()){
            List<ClassRepresentation> equivalentClasses = new ArrayList<>();
            equivalentClasses.add(classRep);
            getEquivalentClasses(classRep,equivalentClasses);
            if(equivalentClasses.size() > 1) {
                EquivalentClassRepresentation equivalentClass = new EquivalentClassRepresentation();
                equivalentClass.addEquivalentClasses(equivalentClasses);
                equivalentClass.setClassNameWithConcatEquivalentClasses();
                classRep.setEquivalentClass(equivalentClass);
                //this is here because of collection of equivalent classes
                for(ClassRepresentation eqClass:equivalentClasses){
                    eqClass.setEquivalentClass(equivalentClass);
                }

                equivalentClassesMapping.add(equivalentClass);
            }
        }else{
            classRep.setEquivalentClass(ret.get());
        }
    }

    public void mapUnionOfClasses(ClassRepresentation classRep) throws Exception {
        Resource unionNode = modelManager.getFirstResource(OWL.UNIONOF,classRep.getResourceValue());
        if(unionNode == null){
            return;
        }
        List<Value> unionOfClasses = modelManager.getRDFCollection(unionNode);
        for (Value unionValue : unionOfClasses)
        {
            if (unionValue.isResource()) {
                ClassRepresentation childClass = getMappedClass((Resource) unionValue);

                if (childClass != null) {
                    classRep.addUnionOf(childClass);
                    if (classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)) {
                        ((AbstractClassRepresentation) classRep).setClassNameWithConcatUnionClasses();
                        childClass.setHasSuperAbstractClass(true);
                    }
                    //mapping union is to subclass
                    //todo
                    //classRep.addSuperClasses(childClass);
                    childClass.addSuperClasses(classRep);
                } else {
                    throw new Exception("Class " + unionValue.stringValue() + " doesn't exist in the ontology.");
                }
            }
        }
    }
    
    public void mapIntersectionOfClass(ClassRepresentation classRep) throws Exception {
        Resource intersectionNode = modelManager.getFirstResource(OWL.INTERSECTIONOF,classRep.getResourceValue());
        if(intersectionNode == null){
            return;
        }
        List<Value> intersectionOfClasses = modelManager.getRDFCollection(intersectionNode);
        for (Value intersectionValue : intersectionOfClasses)
        {
            if (intersectionValue.isResource()) {
                ClassRepresentation childClass = getMappedClass((Resource) intersectionValue);

                if (childClass != null) {
                    classRep.addIntersectionOf(childClass);
                    if (classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)) {
                        ((AbstractClassRepresentation) classRep).setClassNameWithConcatIntersectionClasses();
                        childClass.setHasSuperAbstractClass(true);
                    }
                    //mapping union is to subclass
                    //todo
                    classRep.addSuperClasses(childClass);
                    childClass.addSubClasses(classRep);
                } else {
                    throw new Exception("Class " + intersectionValue.stringValue() + " doesn't exist in the ontology.");
                }
            }
        }
    }

    public void mapComplementOfClass(ClassRepresentation classRep) throws Exception {
        Resource complementResource = modelManager.getFirstResource(OWL.COMPLEMENTOF,classRep.getResourceValue());
        if(complementResource == null){
            return;
        }

        ClassRepresentation complementOfClass = getMappedClass( complementResource);

        if (complementOfClass != null) {
            classRep.setComplementOf(complementOfClass);
            if (classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)) {
                ((AbstractClassRepresentation) classRep).setClassNameWithConcatComplementClass();
                complementOfClass.setHasSuperAbstractClass(true);
            }
            List<ClassRepresentation> superClasses = complementOfClass.getSuperClasses();
            classRep.addAllSuperClasses(superClasses);
            for(ClassRepresentation superClass:superClasses){
                superClass.addSubClasses(classRep);
            }
            //superClasses.add(classRep);
                //mapping union is to subclass
                //todo
                //classRep.addSuperClasses(childClass);
                //childClass.addSuperClasses(classRep);
        } else {
            throw new Exception("Class " + complementResource.stringValue() + " doesn't exist in the ontology.");
        }
    }

    public void mapComments(ClassRepresentation classRep){
        Set<Literal> allComments = modelManager.getAllLiteralObjects(COMMENT_PREDICATE_IRIS,classRep.getResourceValue());
        for(Literal comment : allComments){
                classRep.addCommentProperty(comment.stringValue());
        }
    }

    public void mapLabels(ClassRepresentation classRep){
        Set<Literal> allLabels = modelManager.getAllLiteralObjects(LABEL_PREDICATE_IRIS,classRep.getResourceValue());
        for(Literal comment : allLabels){
            classRep.addLabelProperty(comment.stringValue());
        }
    }

    public void mapCreator(ClassRepresentation classRep){
        Literal creator = modelManager.getFirstLiteralObject(CREATOR_PREDICATE_IRIS, classRep.getResourceValue());
        if ( creator!= null ){
                classRep.setCreator(creator.stringValue());
        }
    }

    public List<Resource> getAllEquivalentResources(ClassRepresentation classRep){
        return classRep.getEquivalentClass()!= null?classRep.getEquivalentClass().getEquivalentClasses().stream().map( eqClass -> eqClass.getResourceValue()).collect(Collectors.toList()) : new ArrayList<>(Arrays.asList(classRep.getResourceValue()));
    }

    /*public void mapProperties(){
        Set<Resource> datatypeProperties = modelManager.getAllSubjects(RDF.TYPE, OWL.DATATYPEPROPERTY);
        for(Resource property : datatypeProperties){
            if(property.isIRI()){
                IRI propertyIRI = (IRI) property;
                Resource domain = modelManager.getFirstSubject(RDFS.DOMAIN, propertyIRI);
                if(domain.isIRI()){
                    ClassRepresentation classRep = getMappedClass((IRI) domain);
                }else if(domain.isBNode()){

                }
                PropertyRepresentation propertyRep = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
                IRI range = modelManager.getFirstIRIObject(RDFS.RANGE, propertyIRI);
                property.setRangeIRI(range);
                property.setIsFunctional(true);
                classRep.addProperties(property);

                mapEquivalentProperties(classRep,property);

                mapPropertyHierarchy(classRep,property);
            }
        }
    }*/


    public void mapProperties(ClassRepresentation classRep){
        List<Resource> classesResources = getAllEquivalentResources(classRep);
        Set<IRI> properties = modelManager.getAllIRISubjects(RDFS.DOMAIN,classesResources);
        for(IRI propertyIRI : properties){
            if(modelManager.existStatementWithIRI(propertyIRI,RDF.TYPE, OWL.DATATYPEPROPERTY)){
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
                IRI range = modelManager.getFirstIRIObject(RDFS.RANGE, propertyIRI);
                property.setRangeResource(range);
                property.setIsFunctional(true);
                classRep.addProperties(property);

                mapEquivalentProperties(classRep,property);

                mapPropertyHierarchy(classRep,property);

            }else if(modelManager.existStatementWithIRI(propertyIRI,RDF.TYPE, OWL.OBJECTPROPERTY)) {
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.OBJECT);
                Resource range = modelManager.getFirstResource(RDFS.RANGE, propertyIRI);
                if (range == null) {
                   // throw new Exception("Missing range value in property " + propertyIRI.stringValue() + ".");
                }
                property.setRangeResource(range);
                //together all classes
                ClassRepresentation rangeClass = getMappedClass(range);
                if (rangeClass == null) {
                    //todo class is not there
                }
                property.setRangeClass(rangeClass);
                classRep.addProperties(property);

                mapInverseProperties(classRep,property);

                mapFunctionalProperties(property);

                mapEquivalentProperties(classRep,property);

                mapPropertyHierarchy(classRep,property);

                mapInverseFunctionalProperties(classRep,property);
            }
        }


    }

    public void mapFunctionalProperties(PropertyRepresentation property){
        boolean isFunctionalProperty = modelManager.existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.FUNCTIONALPROPERTY);
        if(isFunctionalProperty) {
            property.setIsFunctional(true);
        }
    }

    public void mapInverseFunctionalProperties(ClassRepresentation classRep, PropertyRepresentation property){
        boolean isInverseFunctionalProperty = modelManager.existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.INVERSEFUNCTIONALPROPERTY);
        if(isInverseFunctionalProperty) {
            PropertyRepresentation inverseProperty = createInverseProperty(classRep, property, Values.iri(classRep.getNamespace() + classRep.getName().toLowerCase()));
            inverseProperty.setIsFunctional(true);
        }
    }


    public void mapInverseProperties(ClassRepresentation classRep, PropertyRepresentation property){
        ClassRepresentation rangeClass = property.getRangeClass();
        Set<IRI> inverseIRIProperties = modelManager.getAllIRISubjects(OWL.INVERSEOF, property.getValueIRI());
        for(IRI inversePropertyIRI :inverseIRIProperties){
            PropertyRepresentation inverseProperty = createInverseProperty(classRep, property, inversePropertyIRI);
            /*PropertyRepresentation inverseProperty = new PropertyRepresentation(inversePropertyIRI.getNamespace(), inversePropertyIRI.getLocalName());
            inverseProperty.setRangeIRI(classRep.getValueIRI());
            inverseProperty.setRangeClass(classRep);
            inverseProperty.setClassName(rangeClass.getName());

            mapFunctionalProperties(inverseProperty);

            rangeClass.addProperties(inverseProperty);*/
            //check OWL.INVERSEFUNCTIONALPROPERTY

            //todo check if can be possible inverse inversed property
            mapInverseProperties(rangeClass,inverseProperty);

            mapEquivalentProperties(rangeClass,inverseProperty);
            mapPropertyHierarchy(rangeClass,inverseProperty);
        }
    }


    public PropertyRepresentation createInverseProperty(ClassRepresentation classRep, PropertyRepresentation property, IRI inversePropertyIRI ){
        PropertyRepresentation inverseProperty = new PropertyRepresentation(inversePropertyIRI.getNamespace(), inversePropertyIRI.getLocalName());
        inverseProperty.setRangeResource(classRep.getResourceValue());
        inverseProperty.setRangeClass(classRep);
        inverseProperty.setClassName(property.getRangeClass().getName());
        inverseProperty.setType(property.getType());
        mapFunctionalProperties(inverseProperty);
        property.getRangeClass().addProperties(inverseProperty);
        return inverseProperty;
    }

    /*public List<IRI> getEquivalentProperties(IRI property, List<IRI> actualEquivalentProperties){
        actualEquivalentProperties.add(property);
        Set<IRI> equivalentProperties = modelManager.getAllIRISubjects(OWL.EQUIVALENTPROPERTY, property);

        for(IRI eqPropertyIRI: equivalentProperties){
            if(!actualEquivalentProperties.contains(eqPropertyIRI) ) {
                getEquivalentProperties(eqPropertyIRI,actualEquivalentProperties);
            }
        }
        return actualEquivalentProperties;
    }*/


    public PropertyRepresentation createSameProperty(PropertyRepresentation property,IRI newPropertyIRI) {
        PropertyRepresentation newPropertyRep = new PropertyRepresentation(newPropertyIRI.getNamespace(), newPropertyIRI.getLocalName());
        newPropertyRep.setRangeResource(property.getRangeResource());
        newPropertyRep.setRangeClass(property.getRangeClass());
        newPropertyRep.setType(property.getType());
        newPropertyRep.setValue(property.getValue());
        newPropertyRep.setClassName(property.getClassName());
        return newPropertyRep;
    }



    public void mapEquivalentProperties(ClassRepresentation classRep, PropertyRepresentation property){
        //List<IRI> equivalentProperties = new ArrayList<>();
        Set<IRI> equivalentProperties = modelManager.getAllIRISubjects(OWL.EQUIVALENTPROPERTY, property.getValueIRI());

        //equivalentProperties = getEquivalentProperties(property.getValueIRI(),equivalentProperties);
       // equivalentProperties.remove(property.getValueIRI());
        for(IRI equivalentProperty:equivalentProperties){
            List<IRI> setIRIs = getAllPropertiesIRIs(classRep);
            if(!setIRIs.contains(equivalentProperty)) {
                PropertyRepresentation equivalentPropertyRep = createSameProperty(property, equivalentProperty);
                //todo need to check if functional property is set in equivalent classes
                equivalentPropertyRep.setIsFunctional(property.isFunctional());
                //mapFunctionalProperties(equivalentPropertyRep);
                equivalentPropertyRep.setIsEquivalentTo(property);
                property.addEquivalentProperty(equivalentPropertyRep);
                classRep.addProperties(equivalentPropertyRep);
                mapEquivalentProperties(classRep, equivalentPropertyRep);
                mapPropertyHierarchy(classRep, equivalentPropertyRep);
            }
        }
    }

    public List<IRI> getAllPropertiesIRIs(ClassRepresentation classRep){
        return classRep.getProperties().stream().map(c -> c.getValueIRI()).collect(Collectors.toList());
    }

    public void mapPropertyHierarchy(ClassRepresentation classRep, PropertyRepresentation property){
        Set<IRI> subProperties = modelManager.getAllIRISubjects(SUBPROPERTY_PREDICATE_IRIS,property.getValueIRI());

        for(IRI subProperty: subProperties){
            List<IRI> setIRIs = getAllPropertiesIRIs(classRep);
            if(!setIRIs.contains(subProperty)) {
                PropertyRepresentation subPropertyRep = createSameProperty(property, subProperty);
                property.addSubProperty(subPropertyRep);
                subPropertyRep.addSuperProperty(property);
                classRep.addProperties(subPropertyRep);
                mapEquivalentProperties(classRep, subPropertyRep);
                mapPropertyHierarchy(classRep, subPropertyRep);
            }
        }
    }

    public void mapImports(OntologyRepresentation ontology){
        Set<IRI> imports = modelManager.getAllIRIObjects(IMPORTS_IRIS,ontology.getValueIRI());
        for(IRI importVal : imports){
            ontology.addImports(importVal.stringValue());
        }
    }

    public void mapPriorVersion(OntologyRepresentation ontology){
        IRI priorVersion = modelManager.getFirstIRIObject(PRIORVERSION_IRIS,ontology.getValueIRI());
        if ( priorVersion!= null ){
            ontology.setPriorVersion(priorVersion.stringValue());
        }
    }

    public void mapOntologyInformations(OntologyRepresentation ontology){
        mapImports(ontology);
        mapPriorVersion(ontology);
    }

    public OntologyRepresentation getOWLOntology(){
        IRI owlOntologyIRI = modelManager.getFirstIRISubject(RDF.TYPE,OWL.ONTOLOGY);
        if(owlOntologyIRI != null) {
            OntologyRepresentation onto = new OntologyRepresentation(owlOntologyIRI.getNamespace(), owlOntologyIRI.getLocalName());
            return onto;
        }
        return null;
    }

    public List<OntologyRepresentation> getOWLOntologies(){
        Set<IRI> owlOntologyIRIs = modelManager.getAllIRISubjects(RDF.TYPE,OWL.ONTOLOGY);
        List<OntologyRepresentation> ontologies = new ArrayList<>();
        for(IRI owlOntologyIRI:owlOntologyIRIs){
            if(!modelManager.getAllIRISubjects(OWL.PRIORVERSION,owlOntologyIRI).isEmpty()){
                continue;
            }
            OntologyRepresentation onto = new OntologyRepresentation(owlOntologyIRI.getNamespace(), owlOntologyIRI.getLocalName());
            ontologies.add(onto);
        }
        return ontologies;
    }

}
