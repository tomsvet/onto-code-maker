
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF

class SerializationModel:

    def getFirstLiteralObject(self,ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,Literal):
                return object
        return None

    def getAllLiteralObjects(self,ontology, predicate, subject):
        allObjectsIRIs = set()
        for  object in self.getAllObjects(ontology,predicate,subject):
            if isinstance(object,Literal):
                allObjectsIRIs.add(object)
        return allObjectsIRIs

    def getFirstIriObject(self,ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,URIRef) :
                return object
        return None

    def getAllIRIObjects(self,ontology, predicate, subject):
        allObjectsIRIs = set()
        for object in self.getAllObjects(ontology,predicate,subject):
            if isinstance(object,URIRef):
                allObjectsIRIs.add( object)
        return allObjectsIRIs

    def getAllObjects(self,ontology, predicate, subject):
        return ontology.objects(subject,predicate)

    def getAllSubjects(self,ontology,predicate,object):
        return ontology.subjects(predicate,object)

    def getFirstIRISubject(self, ontology, predicate, object):
        for subject in ontology.subjects(predicate,object):
            if isinstance(subject,URIRef):
                return subject
        return None

    def getAllIRISubjects(self, ontology, predicate, object):
        allSubjectsIRIs = set()
        for subject in ontology.subjects(predicate,object):
            if isinstance(subject,URIRef):
                allSubjectsIRIs.add( subject)
        return allSubjectsIRIs

    def getAllResourceObjects(self, ontology, predicate, subject):
        allObjectsIRIs = set()
        for object in ontology.objects(subject,predicate):
            if type(subject) in [BNode, URIRef]:
                allObjectsIRIs.add(object)
        return allObjectsIRIs

    def getModelRDFCollection(self, ontology, node):
        return Collection(ontology, node)

    def getRDFCollection(self,ontology,node):
        return Collection(ontology, node)

    def setRDFCollection(self,ontology, subject, predicate, objects):
        if(len(objects) != 0):
            bnode_list = BNode()
            ontology.add((subject, predicate, bnode_list))
            os = Collection(ontology, bnode_list)
            for item in objects:
                os.append(item.getIri())

    def setLiteralsRDFCollection(self,ontology, subject, predicate, objects):
        if(len(objects) != 0):
            bnode_list = BNode()
            ontology.add((subject, predicate, bnode_list))
            os = Collection(ontology, bnode_list)
            for item in objects:
                os.append(Literal(item))

    def getSubjectOfCollectionValue(self,ontology, predicate, object):
        if (None, predicate, object) in ontology:
            return self.getFirstIRISubject(ontology,predicate,object)
        else:
            for sub, pre, obj in ontology.triples((None,None,object)):
                if isinstance(sub,BNode) and (None, predicate, sub) in ontology:
                    return self.getFirstIRISubject(ontology,predicate,sub)
                elif isinstance(sub,BNode):
                    retVal = self.getSubjectOfCollectionValue(ontology, predicate, sub)
                    if retVal is not None:
                        return retVal



