package ontology.tool.mapper;

import ontology.tool.mapper.representations.*;
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
    List<IRI> RESTRICTION_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(OWL.CARDINALITY,OWL.MAXCARDINALITY,OWL.MAXQUALIFIEDCARDINALITY,OWL.MINCARDINALITY,OWL.MINQUALIFIEDCARDINALITY,OWL.QUALIFIEDCARDINALITY,OWL.ALLVALUESFROM,OWL.HASVALUE,OWL.SOMEVALUESFROM,OWL.TARGETVALUE));

    //List<ClassRepresentation> ontologyClasses = new ArrayList<>();
    private HashMap<Resource, ClassRepresentation> ontologyClasses = new HashMap<>();
    //List<AbstractClassRepresentation> abstractClassesMapping = new ArrayList<>();
    List<EquivalentClassRepresentation> equivalentClassesMapping = new ArrayList<>();

    private HashMap<IRI, PropertyRepresentation> properties = new HashMap<>();



    public OntologyMapper(Model model){
        this.modelManager = new ModelManager(model);
    }

    public Collection<ClassRepresentation> getCollectionOfMappedClasses(){
        return ontologyClasses.values();
    }

    public Collection<PropertyRepresentation> getCollectionOfMappedProperties(){
        return properties.values();
    }

    public HashMap<IRI, PropertyRepresentation> getMappedProperties(){
        return properties;
    }

    public HashMap<Resource, ClassRepresentation> getMappedClasses(){
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

    /*public ClassRepresentation getMappedClass(Resource classResource){
        //Optional<ClassRepresentation> res = ontologyClasses.stream().filter(classRep -> !classRep.isSameName()? classRep.getResourceValue().equals(classResource): classRep.getResourceValue().equals(Values.iri(classResource.stringValue() + classRep.getSameNameIndex()))).findFirst();
        return ontologyClasses.get(classResource);
    }*/

    /*public AbstractClassRepresentation getMappedAbstractClassesMapping(Resource classId){
        Optional<AbstractClassRepresentation> res = abstractClassesMapping.stream().filter(classRep -> classRep.getResourceValue().equals(classId)).findFirst();
        return res.orElse(null);
    }*/

    public boolean isMappedClass(Value classValue){
        Optional<ClassRepresentation> res = getCollectionOfMappedClasses().stream().filter(classRep -> classRep.getResourceValue().equals(classValue)).findFirst();
        return res.isPresent();
    }

    public List<AbstractClassRepresentation> getAbstractClassReps(){
        return getCollectionOfMappedClasses().parallelStream().filter(classRep -> classRep instanceof AbstractClassRepresentation).map(foundClass -> (AbstractClassRepresentation)foundClass).collect(Collectors.toList());
    }

    public void mapping() throws Exception {

        mapClasses();

        // first need to map class equivalence and then hierarchy
        for(ClassRepresentation classRep:getCollectionOfMappedClasses()){
            mapUnionOfClasses(classRep);
            mapIntersectionOfClass(classRep);
            mapComplementOfClass(classRep);
            mapDisjointWith(classRep);

            mapEquivalentClasses(classRep);
            mapClassHierarchy(classRep);

            mapComments(classRep);
            mapLabels(classRep);
            mapCreator(classRep);
        }


        mapDataTypeProperties();
        mapObjectProperties();

        mapPropertyRelationShips();



        // then can map class properties
        for(ClassRepresentation classRep:getCollectionOfMappedClasses()){
            if(classRep.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)){
                ((AbstractClassRepresentation)classRep).setAbstractClassName();
            }
            if(classRep.getEquivalentClass() != null){
                classRep.getEquivalentClass().setClassNameWithConcatEquivalentClasses();
            }
            //findOutInterfaceClassValueOfClass(classRep);

            /*mapComments(classRep);
            mapLabels(classRep);
            mapCreator(classRep);
            mapProperties(classRep);*/
        }


        //change duplicate names
       /* for(ClassRepresentation classRep:ontologyClasses){
            boolean isDuplicateName = true;
            int i = 0;
            String name = classRep.getName();
            while(isDuplicateName){
                if(getMappedClassesWithSameName(classRep) == null){
                    isDuplicateName = false;
                }else{
                    classRep.setName(name + i);
                    i++;
                }
            }
        }*/

        //remove duplicate abstract classes
        for(AbstractClassRepresentation classRep:getAbstractClassReps()){
            if(!classRep.isToRemove()) {
                AbstractClassRepresentation existAbstract = getSameAbstractClassReps(classRep, classRep.getName());
                if (existAbstract != null) {
                    classRep.getSuperClasses().removeAll(existAbstract.getSuperClasses());
                    classRep.getSuperClasses().addAll(existAbstract.getSuperClasses());
                    for(ClassRepresentation superClass:existAbstract.getSuperClasses()){
                        superClass.getSubClasses().remove(existAbstract);
                        //superClass.addSubClasses(classRep);
                    }
                    classRep.getSubClasses().removeAll(existAbstract.getSubClasses());
                    classRep.getSubClasses().addAll(existAbstract.getSubClasses());
                    for(ClassRepresentation subClass:existAbstract.getSubClasses()){
                        subClass.getSuperClasses().remove(existAbstract);
                        //subClass.addSuperClasses(classRep);
                    }
                    classRep.getProperties().removeAll(existAbstract.getProperties());
                    classRep.getProperties().addAll(existAbstract.getProperties());
                    existAbstract.setToRemove(true);
                    ontologyClasses.remove(existAbstract.getResourceValue());
                }
            }
        }



    }

    public ClassRepresentation getMappedClassesWithSameName(String className){
        Optional<ClassRepresentation> duplicateClassRep = ontologyClasses.values().parallelStream().filter(x -> x.getName().equals(className)).findFirst();
        return duplicateClassRep.isPresent()?duplicateClassRep.get():null;
    }


    private void checkAndChangeDuplicateName(ClassRepresentation classRep){
        boolean isDuplicateName = true;
        int i = 2;
        String name = classRep.getName();
        while(isDuplicateName){
            if(getMappedClassesWithSameName(classRep.getName()) == null){
                isDuplicateName = false;
            }else{
                classRep.setName(name + i);
                classRep.setSameNameIndex(i);
                i++;
            }
        }
    }

    public void mapClasses(){
        ontologyClasses = new HashMap<>();
        Set<Resource> classes = getClasses();
        for(Resource classResource:classes){
            if(classResource.isIRI()){
                NormalClassRepresentation classRep = new NormalClassRepresentation(((IRI) classResource).getNamespace(),((IRI) classResource).getLocalName());
                checkAndChangeDuplicateName(classRep);
                ontologyClasses.put(classResource,classRep);
            }else if(classResource.isBNode()){
                AbstractClassRepresentation abstractClassRep = new AbstractClassRepresentation(((BNode) classResource).getID());
                ontologyClasses.put(classResource,abstractClassRep);
            }
        }
    }

    private Set<Resource> getClasses(){
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
            if(superClass.isBNode() ){
                if(modelManager.isCollection((Resource)superClass)) {
                    Set<Value> subClassOfClasses = modelManager.getRDFCollection((Resource) superClass);
                    finalSuperList.remove(superClass);
                    finalSuperList.addAll(subClassOfClasses);
                }else{
                    checkAndMapRestriction(classRep,superClass, RestrictionRepresentation.RESTRICTION_IN_TYPE.SUBCLASS);
                    if(!ontologyClasses.containsKey(superClass)) {
                        finalSuperList.remove(superClass);
                    }
                }
            }
        }
        for(Value superClassValue: finalSuperList){
            if(superClassValue.isResource()) {
                //Resource superClassResource = (Resource) superClassValue;
                ClassRepresentation superClassRep = ontologyClasses.get(((Resource) superClassValue));

                if (superClassRep != null) {
                    classRep.addSuperClasses(superClassRep);
                    superClassRep.addSubClasses(classRep);
                    if (classRep.getEquivalentClass() != null && !classRep.getEquivalentClass().getSuperClasses().contains(superClassRep)) {
                        classRep.getEquivalentClass().addSuperClasses(superClassRep);
                    }
                } else {
                    //todo test this error
                    throw new Exception("Class " + superClassValue.stringValue() + " doesn't exist in the ontology.");
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
            if(eqClass.isBNode() ){
                Resource eqResource = (Resource) eqClass;
                if(modelManager.isCollection(eqResource)) {
                    Set<Value> equivalentOfClasses = modelManager.getRDFCollection(eqResource);
                    finalEqList.remove(eqClass);
                    finalEqList.addAll(equivalentOfClasses);
                }else {
                    checkAndMapRestriction(classRep,eqResource, RestrictionRepresentation.RESTRICTION_IN_TYPE.EQUIVALENT);
                    if(!ontologyClasses.containsKey(eqClass)) {
                        finalEqList.remove(eqClass);
                    }
                }
            }
        }

        for(Value eqClass: finalEqList){

            if(eqClass.isResource()) {
                ClassRepresentation eqClassRep = ontologyClasses.get((Resource) eqClass);

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

    public void mapDisjointWith(ClassRepresentation classRep) throws Exception {
        Set<Value> disjointWithValues = modelManager.getAllObjects(OWL.DISJOINTWITH,classRep.getResourceValue());
        Set<Value> finalDisjointList = new HashSet<>(disjointWithValues);
        //to find collections
        for(Value disValue: disjointWithValues){
            if(disValue.isBNode() ){
                Resource disResource = (Resource) disValue;
                if(modelManager.isCollection(disResource)) {
                    Set<Value> disjointWithClassValue = modelManager.getRDFCollection(disResource);
                    finalDisjointList.remove(disValue);
                    finalDisjointList.addAll(disjointWithClassValue);
                }else {
                    checkAndMapRestriction(classRep,disValue, RestrictionRepresentation.RESTRICTION_IN_TYPE.EQUIVALENT);
                    if(!ontologyClasses.containsKey(disValue)) {
                        finalDisjointList.remove(disValue);
                    }
                }
            }
        }

        for(Value disjointValue:finalDisjointList){
            if(disjointValue.isResource()) {
                ClassRepresentation disjointClassRep = ontologyClasses.get((Resource) disjointValue);
                if (disjointClassRep != null) {
                    classRep.addDisjointWith(disjointClassRep);
                }else{
                    throw new Exception("Class " + disjointValue.stringValue() + " doesn't exist in the ontology. This class is used in disjointWith tag in class " + classRep.getResourceValue().stringValue() + ".");
                }
            }
        }

    }

    public boolean checkAndMapRestriction(ClassRepresentation classRep, Value unionValue, RestrictionRepresentation.RESTRICTION_IN_TYPE type){
        BNode unionBnode = (BNode) unionValue;
        if(modelManager.existStatementWithIRI(unionBnode,RDF.TYPE,OWL.RESTRICTION)){
            IRI onProperty = modelManager.getFirstIRIObject(OWL.ONPROPERTY, unionBnode);
            Statement restrictionType = modelManager.getFirstStatementWithIRI(unionBnode,RESTRICTION_PREDICATE_IRIS,null);
            RestrictionRepresentation restRep = new RestrictionRepresentation("Restr" + onProperty.getLocalName().substring(0, 1).toUpperCase() + onProperty.getLocalName().substring(1));
            restRep.setOnProperty(onProperty);
            restRep.setType(restrictionType.getPredicate().stringValue());
            restRep.setValue(restrictionType.getObject().stringValue());
            restRep.setRestrictionIn(type);
            if(type.equals(RestrictionRepresentation.RESTRICTION_IN_TYPE.UNIONOF)){
               // restRep.setRestrictionIn(RestrictionRepresentation.RESTRICTION_IN_TYPE.UNIONOF);
                classRep.addRestriction(restRep);
                classRep.addUnionOf(restRep);
                ((AbstractClassRepresentation) classRep).setCreateOf(AbstractClassRepresentation.ABSTRACT_CREATE_OF.UNIONOF);
                ((AbstractClassRepresentation) classRep).setAbstractClassName();
            }else if(type.equals(RestrictionRepresentation.RESTRICTION_IN_TYPE.INTERSECTIONOF)){
               // restRep.setRestrictionIn(RestrictionRepresentation.RESTRICTION_IN_TYPE.INTERSECTIONOF);
                classRep.addRestriction(restRep);
                ((AbstractClassRepresentation) classRep).setCreateOf(AbstractClassRepresentation.ABSTRACT_CREATE_OF.INTERSECTIONOF);
                ((AbstractClassRepresentation) classRep).setAbstractClassName();
                classRep.addIntersectionOf(restRep);
            }else if(type.equals(RestrictionRepresentation.RESTRICTION_IN_TYPE.COMPLEMENT)){
                //restRep.setRestrictionIn(RestrictionRepresentation.RESTRICTION_IN_TYPE.COMPLEMENT);
                classRep.addRestriction(restRep);
                classRep.setComplementOf(restRep);
                ((AbstractClassRepresentation) classRep).setCreateOf(AbstractClassRepresentation.ABSTRACT_CREATE_OF.COMPLEMENT);
                ((AbstractClassRepresentation) classRep).setAbstractClassName();
            }else if(type.equals(RestrictionRepresentation.RESTRICTION_IN_TYPE.EQUIVALENT)){
                //restRep.setRestrictionIn(RestrictionRepresentation.RESTRICTION_IN_TYPE.EQUIVALENT);
                classRep.addRestriction(restRep);
            }else if(type.equals(RestrictionRepresentation.RESTRICTION_IN_TYPE.DISJOINTWITH) || type.equals(RestrictionRepresentation.RESTRICTION_IN_TYPE.SUBCLASS)){
                classRep.addRestriction(restRep);
            }
            restRep.setClassName(classRep.getName());

            return true;
        }
        return  false;
    }

    public void mapUnionOfClasses(ClassRepresentation classRep) throws Exception {
        Resource unionNode = modelManager.getFirstResource(OWL.UNIONOF,classRep.getResourceValue());
        if(unionNode == null){
            return;
        }
        Set<Value> unionOfClasses = modelManager.getRDFCollection(unionNode);
        for (Value unionValue : unionOfClasses)
        {
            if (unionValue.isResource()) {
                ClassRepresentation childClass = ontologyClasses.get((Resource) unionValue);

                if (childClass != null) {
                    classRep.addUnionOf(childClass);
                    if (classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)) {
                        ((AbstractClassRepresentation) classRep).setCreateOf(AbstractClassRepresentation.ABSTRACT_CREATE_OF.UNIONOF);
                        ((AbstractClassRepresentation) classRep).setAbstractClassName();
                        childClass.setHasSuperAbstractClass(true);
                    }
                    //mapping union is to subclass
                    childClass.addSuperClasses(classRep);
                    classRep.addSubClasses(childClass);
                } else {
                    if (unionValue.isBNode()) {
                        checkAndMapRestriction(classRep,unionValue,RestrictionRepresentation.RESTRICTION_IN_TYPE.UNIONOF);
                        continue;
                    }
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
        Set<Value> intersectionOfClasses = modelManager.getRDFCollection(intersectionNode);
        for (Value intersectionValue : intersectionOfClasses)
        {
            if (intersectionValue.isResource()) {
                ClassRepresentation childClass = ontologyClasses.get((Resource) intersectionValue);

                if (childClass != null) {
                    classRep.addIntersectionOf(childClass);
                    if (classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)) {
                        ((AbstractClassRepresentation) classRep).setCreateOf(AbstractClassRepresentation.ABSTRACT_CREATE_OF.INTERSECTIONOF);
                        ((AbstractClassRepresentation) classRep).setAbstractClassName();
                        childClass.setHasSuperAbstractClass(true);
                    }
                    classRep.addSuperClasses(childClass);
                    childClass.addSubClasses(classRep);
                } else {
                    if (intersectionValue.isBNode()) {
                        checkAndMapRestriction(classRep,intersectionValue,RestrictionRepresentation.RESTRICTION_IN_TYPE.INTERSECTIONOF);
                        continue;

                    }
                    throw new Exception("Class " + intersectionValue.stringValue() + " doesn't exist in the ontology.");
                }
            }
        }
    }

    public AbstractClassRepresentation getSameAbstractClassReps(AbstractClassRepresentation actualClassRep,String name ){
        Optional<ClassRepresentation> retVal = getCollectionOfMappedClasses().parallelStream().filter(classRep -> classRep instanceof AbstractClassRepresentation && !classRep.equals(actualClassRep)&& classRep.getName().equals(name)).findFirst();//.map(foundClass -> (AbstractClassRepresentation)foundClass).collect(Collectors.toList());
        return retVal.isPresent()? (AbstractClassRepresentation)retVal.get():null;
    }

    public void mapComplementOfClass(ClassRepresentation classRep) throws Exception {
        Resource complementResource = modelManager.getFirstResource(OWL.COMPLEMENTOF,classRep.getResourceValue());
        if(complementResource == null){
            return;
        }

        ClassRepresentation complementOfClass = ontologyClasses.get(complementResource);

        if (complementOfClass != null) {
            classRep.setComplementOf(complementOfClass);
            if (classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)) {
                ((AbstractClassRepresentation) classRep).setCreateOf(AbstractClassRepresentation.ABSTRACT_CREATE_OF.COMPLEMENT);
                ((AbstractClassRepresentation) classRep).setAbstractClassName();
                complementOfClass.setHasSuperAbstractClass(true);
            }
            List<ClassRepresentation> superClasses = complementOfClass.getSuperClasses();
            List<ClassRepresentation> filteredSuperClasses = superClasses.stream().filter(ent -> !ent.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)).collect(Collectors.toList());
            classRep.addAllSuperClasses(filteredSuperClasses);
            for(ClassRepresentation superClass:filteredSuperClasses){
                superClass.addSubClasses(classRep);
            }
        } else {
            if(!(complementResource.isBNode() && checkAndMapRestriction(classRep,complementResource,RestrictionRepresentation.RESTRICTION_IN_TYPE.COMPLEMENT))) {
                throw new Exception("Class " + complementResource.stringValue() + " doesn't exist in the ontology.");
            }
        }
    }

    public void mapComments(EntityRepresentation entityRep){
        Set<Literal> allComments = modelManager.getAllLiteralObjects(COMMENT_PREDICATE_IRIS,entityRep.getResourceValue());
        for(Literal comment : allComments){
            entityRep.addCommentProperty(comment.stringValue());
        }
    }

    public void mapLabels(EntityRepresentation entityRep){
        Set<Literal> allLabels = modelManager.getAllLiteralObjects(LABEL_PREDICATE_IRIS,entityRep.getResourceValue());
        for(Literal comment : allLabels){
            entityRep.addLabelProperty(comment.stringValue());
        }
    }

    public void mapCreator(EntityRepresentation entityRep){
        Literal creator = modelManager.getFirstLiteralObject(CREATOR_PREDICATE_IRIS, entityRep.getResourceValue());
        if ( creator!= null ){
            entityRep.setCreator(creator.stringValue());
        }
    }

    public List<Resource> getAllEquivalentResources(ClassRepresentation classRep){
        return classRep.getEquivalentClass()!= null?classRep.getEquivalentClass().getEquivalentClasses().stream().map( eqClass -> eqClass.getResourceValue()).collect(Collectors.toList()) : new ArrayList<>(Arrays.asList(classRep.getResourceValue()));
    }

    public void findOutInterfaceClassValueOfClass(ClassRepresentation generatedClass){
        if(!(generatedClass instanceof AbstractClassRepresentation && generatedClass.isUnionOf())) {
            if (generatedClass.hasSubClass() && !generatedClass.isHasInterface()) {
                //If SubClasses Have More Super Classes
                // this is needed when subclasses of generated Class  have more than one superclass because it need extends more classes (in java only interfaces)
                List<ClassRepresentation> resultList = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getSuperClasses().size() > 1).collect(Collectors.toList());
                boolean result1 = false;
                if (!resultList.isEmpty()) {
                    result1 = true;
                    generatedClass.getSuperClasses().parallelStream().forEach(superClass-> superClass.setHasInterface(true));
                    /*List<ClassRepresentation> res = resultList.parallelStream().filter(classRep -> classRep.getSuperClasses().size() > 2 || classRep.getSuperClasses().get(0).getClassType() == classRep.getSuperClasses().get(1).getClassType()).collect(Collectors.toList());
                    if (res.size() == resultList.size()) {
                        result1 = true;
                    }*/
                }

                // if subclass is interface
                if(!result1){
                    List<ClassRepresentation> resultList2 = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.isHasInterface()).collect(Collectors.toList());
                    result1 = !resultList2.isEmpty();
                }

                // if subclasses have equivalent class
                Optional<ClassRepresentation> result2 = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getEquivalentClass() != null).findFirst();

                //Optional<ClassRepresentation> result3 = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getSuperClasses().size() == 2 && classRep.getSuperClasses().get(0).getClassType() != classRep.getSuperClasses().get(1).getClassType()).findFirst();

                generatedClass.setHasInterface(result1 || result2.isPresent());
            }
        }
    }


    /**
     * These methods map properties
     *
     * @param property
     * @return
     */

    private ClassRepresentation getDomainClass(PropertyRepresentation property){
        Resource propertyDomain = modelManager.getFirstResource(RDFS.DOMAIN,property.getValueIRI());
        ClassRepresentation domainClass = ontologyClasses.get(propertyDomain);
        return domainClass;
    }

    private void mapDomain(PropertyRepresentation property) {
        ClassRepresentation domainClass = getDomainClass(property);
        mapDomain(domainClass,property);
    }

    private void mapDomain(ClassRepresentation domainClass,PropertyRepresentation property){
        if(domainClass != null) {
            if(domainClass.getEquivalentClass() != null) {
                for (ClassRepresentation eqClasses : domainClass.getEquivalentClass().getEquivalentClasses()) {
                    eqClasses.addProperties(property);
                }
            }else{
                domainClass.addProperties(property);
            }
            property.setClassName(domainClass.getName());
        }
    }

    public void mapDataTypeProperties(){
        //List<PropertyRepresentation> allDatatypeProperties =  new ArrayList<>();
        Set<IRI> datatypeProperties = modelManager.getAllIRISubjects(RDF.TYPE, OWL.DATATYPEPROPERTY);
        for(IRI propertyIRI : datatypeProperties){
            PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
            property.setType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
            mapDomain(property);
            IRI propertyRange = modelManager.getFirstIRIObject(RDFS.RANGE, propertyIRI);
            property.setRangeResource(propertyRange);

            mapFunctionalProperties(property);
            //mapEquivalentProperties(property);
            //mapPropertyHierarchy(property);

            //datatypes properties cannot be inverse and inversefunctional

            mapComments(property);
            mapCreator(property);
            mapLabels(property);
            properties.put(propertyIRI,property);
        }
    }

    public void mapObjectProperties() throws Exception {
        Set<IRI> objectProperties = modelManager.getAllIRISubjects(RDF.TYPE, OWL.OBJECTPROPERTY);
        for(IRI propertyIRI : objectProperties){
            PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
            property.setType(PropertyRepresentation.PROPERTY_TYPE.OBJECT);
            mapDomain(property);
            Resource propertyRange = modelManager.getFirstResource(RDFS.RANGE, propertyIRI);
            if (propertyRange != null) {
                property.setRangeResource(propertyRange);
                if (propertyRange.isIRI()) {

                }
                //together all classes
                ClassRepresentation rangeClass = ontologyClasses.get(propertyRange);
                if (rangeClass == null) {
                    if (propertyRange.isBNode()) {
                        throw new Exception("Range must be class. Unionof,Intersection or complement is supported only in abstract class.");
                        //it is abstract node but it is not defined as class
                            /*BNode classBNode = (BNode) range;
                            AbstractClassRepresentation abstractClassRep = new AbstractClassRepresentation(classBNode.getID());
                            mapUnionOfClasses(abstractClassRep);
                            mapIntersectionOfClass(abstractClassRep);
                            mapComplementOfClass(abstractClassRep);
                            rangeClass = abstractClassRep;*/
                    } else {
                        throw new Exception("Class " + propertyRange.stringValue() + " doesn't exist in the ontology.");
                    }

                }
                property.setRangeClass(rangeClass);
            }


            mapFunctionalProperties(property);

            mapComments(property);
            mapCreator(property);
            mapLabels(property);

            /*mapInverseProperties(classRep,property);

            mapEquivalentProperties(classRep,property);

            mapPropertyHierarchy(classRep,property);

            mapInverseFunctionalProperties(classRep,property);*/

            properties.put(propertyIRI,property);
        }
    }

    public void mapPropertyRelationShips(){
        Collection<PropertyRepresentation> propertiesValues = new ArrayList<>();
        propertiesValues.addAll(properties.values());
        for(PropertyRepresentation property: propertiesValues){
            ClassRepresentation domainClass = getDomainClass(property);

            mapEquivalentProperties(domainClass,property);
            if(property.getType().equals(PropertyRepresentation.PROPERTY_TYPE.DATATYPE)){
                mapPropertyHierarchy(domainClass,property);
            }else{
                mapInverseProperties(domainClass,property);
                mapPropertyHierarchy(domainClass,property);
                mapInverseFunctionalProperties(domainClass,property);
            }
        }
    }

    public void mapPropertyRelationShips2(){
        Collection<PropertyRepresentation> propertiesValues = new ArrayList<>();
        propertiesValues.addAll(properties.values().stream().filter(p->! p.getClassName().isEmpty()).collect(Collectors.toList()));
        for(PropertyRepresentation property: propertiesValues){
            ClassRepresentation domainClass = getDomainClass(property);

            mapEquivalentProperties(domainClass,property);
            mapPropertyHierarchy(domainClass,property);
            if(property.getType().equals(PropertyRepresentation.PROPERTY_TYPE.OBJECT)){
                mapInverseProperties(domainClass,property);
                mapInverseFunctionalProperties(domainClass,property);
            }
        }
    }


    public void mapProperties(ClassRepresentation classRep) throws Exception {
        List<Resource> classesResources = getAllEquivalentResources(classRep);
        //todo test this classesResources.addAll(modelManager.getSubjectOfCollectionValue(OWL.UNIONOF,classesResources));
        Set<IRI> properties = modelManager.getAllIRISubjects(RDFS.DOMAIN,classesResources);
        for(IRI propertyIRI : properties){
            if(modelManager.existStatementWithIRI(propertyIRI,RDF.TYPE, OWL.DATATYPEPROPERTY)){
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
                IRI range = modelManager.getFirstIRIObject(RDFS.RANGE, propertyIRI);
                property.setRangeResource(range);
                //property.setIsFunctional(true);
                classRep.addProperties(property);

                mapFunctionalProperties(property);
                mapEquivalentProperties(classRep,property);
                mapPropertyHierarchy(classRep,property);

                //datatypes properties cannot be inverse and inversefunctional

            }else if(modelManager.existStatementWithIRI(propertyIRI,RDF.TYPE, OWL.OBJECTPROPERTY)) {
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.OBJECT);
                Resource range = modelManager.getFirstResource(RDFS.RANGE, propertyIRI);
                if (range == null) {
                    //ignore property without range
                   continue;
                }
                property.setRangeResource(range);
                //together all classes
                ClassRepresentation rangeClass = ontologyClasses.get(range);
                if (rangeClass == null) {
                    if(range.isBNode()){
                        throw new Exception("Range must be class. Unionof,Intersection or complement is supported only in abstract class.");
                        //it is abstract node but it is not defined as class
                        /*BNode classBNode = (BNode) range;
                        AbstractClassRepresentation abstractClassRep = new AbstractClassRepresentation(classBNode.getID());
                        mapUnionOfClasses(abstractClassRep);
                        mapIntersectionOfClass(abstractClassRep);
                        mapComplementOfClass(abstractClassRep);
                        rangeClass = abstractClassRep;*/
                    }else {
                        throw new Exception("Class " + range.stringValue() + " doesn't exist in the ontology.");
                    }

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

    public PropertyRepresentation createInverseProperty(ClassRepresentation classRep, PropertyRepresentation property, IRI inversePropertyIRI ){
        List<IRI> setIRIs = getAllPropertiesIRIs(property.getRangeClass());
        if (setIRIs.contains(inversePropertyIRI)){
            inversePropertyIRI = Values.iri(property.getStringIRI()+"Inverse");
        }
        PropertyRepresentation inverseProperty = new PropertyRepresentation(inversePropertyIRI.getNamespace(), inversePropertyIRI.getLocalName());
        inverseProperty.setRangeResource(classRep.getResourceValue());
        inverseProperty.setRangeClass(classRep);
        //inverseProperty.setClassName(property.getRangeClass().getName());
        inverseProperty.setType(property.getType());
        //property.getRangeClass().addProperties(inverseProperty);
        mapDomain(property.getRangeClass(),inverseProperty);
        properties.put(inversePropertyIRI,inverseProperty);
        return inverseProperty;
    }

    public void mapInverseFunctionalProperties(ClassRepresentation classRep, PropertyRepresentation property){
        boolean isInverseFunctionalProperty = modelManager.existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.INVERSEFUNCTIONALPROPERTY);
        if(isInverseFunctionalProperty) {
            PropertyRepresentation inverseProperty = createInverseProperty(classRep, property, Values.iri(classRep.getNamespace() + classRep.getName().toLowerCase()));
            inverseProperty.setIsFunctional(true);
            inverseProperty.setInverseFunctionalOf(property);
            property.setInverseFunctionalTo(inverseProperty);
        }
    }

    public void mapInverseProperties(ClassRepresentation classRep, PropertyRepresentation property){
        Set<IRI> inverseIRIProperties = modelManager.getAllIRISubjects(OWL.INVERSEOF, property.getValueIRI());
        for(IRI inversePropertyIRI :inverseIRIProperties){
            PropertyRepresentation inverseProperty =properties.get(inversePropertyIRI);
            inverseProperty.setRangeResource(classRep.getResourceValue());
            inverseProperty.setRangeClass(classRep);
            //inverseProperty.setClassName(property.getRangeClass().getName());
            inverseProperty.setType(property.getType());
            //property.getRangeClass().addProperties(inverseProperty);
            mapDomain(property.getRangeClass(),inverseProperty);
            inverseProperty.setInverseOf(property);
            property.addInverseTo(inverseProperty);
        }
    }

    /*public PropertyRepresentation createSameProperty(PropertyRepresentation property,IRI newPropertyIRI) {
        PropertyRepresentation newPropertyRep = new PropertyRepresentation(newPropertyIRI.getNamespace(), newPropertyIRI.getLocalName());
        newPropertyRep.setRangeResource(property.getRangeResource());
        newPropertyRep.setRangeClass(property.getRangeClass());
        newPropertyRep.setType(property.getType());
        newPropertyRep.setValue(property.getValue());
        newPropertyRep.setClassName(property.getClassName());
        return newPropertyRep;
    }*/

    private Set<Value> analyseCollectionsFromValues(Set<Value> collectionOfValues){
        Set<Value> finalList = new HashSet<>(collectionOfValues);
        for(Value value: collectionOfValues){
            if(value.isBNode()){
                if(modelManager.isCollection((Resource)value)) {
                    Set<Value> collectionValues = modelManager.getRDFCollection((Resource) value);
                    finalList.remove(value);
                    Set<Value> values = analyseCollectionsFromValues(collectionValues);
                    finalList.addAll(values);
                }else{
                    finalList.remove(value);
                }
            }
        }

        return finalList;
    }

    private PropertyRepresentation findDomainInEqOrSub(PropertyRepresentation property){
        boolean eq = true;
        IRI propertyIRI = modelManager.getFirstIRIObject(OWL.EQUIVALENTPROPERTY, property.getResourceValue());
        if(propertyIRI == null){
            propertyIRI = modelManager.getFirstIRIObject(RDFS.SUBPROPERTYOF, property.getResourceValue());
            eq=false;
        }

        if(propertyIRI != null){
            PropertyRepresentation propertyN = properties.get(propertyIRI);
            if(getDomainClass(propertyN) == null){
                PropertyRepresentation prop = findDomainInEqOrSub(propertyN);
               /* if(eq){
                    mainProperty.addEquivalentProperty(prop);
                }*/
                return prop;
            }else{
                return propertyN;
            }
        }
        return null;
    }

    public void mapEquivalentProperties(ClassRepresentation classRep, PropertyRepresentation property){
        Set<Resource> equivalentProperties = modelManager.getAllSubjects(OWL.EQUIVALENTPROPERTY, property.getResourceValue());
        //todo is this needed here ?
        modelManager.getSubjectOfValue(OWL.EQUIVALENTPROPERTY, property.getResourceValue());

        //to find equivalent collections
        Set<Value> eqPropertyValues = new HashSet<>(equivalentProperties);
        Set<Value> finalEqList =analyseCollectionsFromValues(eqPropertyValues);

        for(Value equivalentProperty:finalEqList){
            if(equivalentProperty.isIRI()) {
                PropertyRepresentation eqProperty = properties.get(equivalentProperty);
                PropertyRepresentation mainProp = property;
                if(classRep == null){
                    classRep = getDomainClass(eqProperty);
                    if(classRep == null){
                        mainProp = findDomainInEqOrSub(property);
                        classRep = getDomainClass(mainProp);
                    }
                }

                eqProperty.setRangeResource(mainProp.getRangeResource());
                eqProperty.setRangeClass(mainProp.getRangeClass());
                eqProperty.setType(mainProp.getType());
                //eqProperty.setValue(property.getValue());
                eqProperty.setClassName(mainProp.getClassName());
                eqProperty.setIsEquivalentTo(property);// todo collection equivalent properties
                eqProperty.addEquivalentProperty(property);
                property.addEquivalentProperty(eqProperty);
                if(!property.equals(mainProp)){
                    eqProperty.addEquivalentProperty(mainProp);
                    mainProp.addEquivalentProperty(eqProperty);
                }
                classRep.addProperties(eqProperty);
                /*if (eqProperty.getType().equals(PropertyRepresentation.PROPERTY_TYPE.OBJECT)) {
                    mapInverseProperties(classRep, eqProperty);
                    mapInverseFunctionalProperties(classRep, eqProperty);
                }*/
            }
        }
    }

    public List<IRI> getAllPropertiesIRIs(ClassRepresentation classRep){
        return classRep.getProperties().stream().map(PropertyRepresentation::getValueIRI).collect(Collectors.toList());
    }

    public void mapPropertyHierarchy(ClassRepresentation classRep, PropertyRepresentation property){
        Set<IRI> subProperties = modelManager.getAllIRISubjects(SUBPROPERTY_PREDICATE_IRIS,property.getValueIRI());
        Set<Value> finalSuperList = new HashSet<>(subProperties);
        finalSuperList = analyseCollectionsFromValues(finalSuperList);

        for(Value subPropertyValue: finalSuperList){
            if(subPropertyValue.isIRI()) {
                PropertyRepresentation subProperty = properties.get(subPropertyValue);
                if(subProperty == null){
                    return;
                }
                PropertyRepresentation mainProp = property;
                if(classRep == null){
                    mainProp = findDomainInEqOrSub(property);
                    classRep = getDomainClass(mainProp);
                }
                subProperty.setRangeResource(mainProp.getRangeResource());
                subProperty.setRangeClass(mainProp.getRangeClass());
                subProperty.setType(mainProp.getType());
                //subProperty.setValue(property.getValue());
                //subProperty.setClassName(property.getClassName());
                property.addSubProperty(subProperty);
                subProperty.addSuperProperty(property);
                //classRep.addProperties(subProperty);
                mapDomain(classRep,subProperty);

            }
        }
    }



    /**
     * These methods map special ontologies properties
     *
     */

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
            return new OntologyRepresentation(owlOntologyIRI.getNamespace(), owlOntologyIRI.getLocalName());
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
            mapOntologyInformations(onto);
            mapComments(onto);
            mapCreator(onto);
            mapLabels(onto);
            ontologies.add(onto);

        }
        return ontologies;
    }

}
