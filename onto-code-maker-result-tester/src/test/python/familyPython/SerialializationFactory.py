from rdflib import URIRef
from rdflib.namespace import RDF
from serialization.HumanSerialization import HumanSerialization
from entities.Human import Human
from serialization.ChildSerialization import ChildSerialization
from entities.Child import Child
from serialization.PersonSerialization import PersonSerialization
from entities.Person import Person
from serialization.FatherSerialization import FatherSerialization
from entities.Father import Father
from serialization.ParentSerialization import ParentSerialization
from entities.Parent import Parent
from serialization.MotherSerialization import MotherSerialization
from entities.Mother import Mother
from serialization.WomanSerialization import WomanSerialization
from entities.Woman import Woman
from serialization.DoggySerialization import DoggySerialization
from entities.Doggy import Doggy
from serialization.CatSerialization import CatSerialization
from entities.Cat import Cat
from serialization.MenSerialization import MenSerialization
from entities.Men import Men
from serialization.ChildlessPersonSerialization import ChildlessPersonSerialization
from entities.ChildlessPerson import ChildlessPerson
from serialization.DogSerialization import DogSerialization
from entities.Dog import Dog
from serialization.PetSerialization import PetSerialization
from entities.Pet import Pet
from Vocabulary import Vocabulary
##
#
#
#   Generated by OntoCodeMaker
##
class SerialializationFactory:
    def getSerializationInstanceFromEntity(self,entity):
        if type(entity) is Human:
            return HumanSerialization()

        if type(entity) is Child:
            return ChildSerialization()

        if type(entity) is Person:
            return PersonSerialization()

        if type(entity) is Father:
            return FatherSerialization()

        if type(entity) is Parent:
            return ParentSerialization()

        if type(entity) is Mother:
            return MotherSerialization()

        if type(entity) is Woman:
            return WomanSerialization()

        if type(entity) is Doggy:
            return DoggySerialization()

        if type(entity) is Cat:
            return CatSerialization()

        if type(entity) is Men:
            return MenSerialization()

        if type(entity) is ChildlessPerson:
            return ChildlessPersonSerialization()

        if type(entity) is Dog:
            return DogSerialization()

        if type(entity) is Pet:
            return PetSerialization()

        return None

    def getSerializationInstanceFromIri(self,classIri):
        if classIri == Vocabulary.HUMAN_CLASS_IRI:
            return HumanSerialization()

        if classIri == Vocabulary.CHILD_CLASS_IRI:
            return ChildSerialization()

        if classIri == Vocabulary.PERSON_CLASS_IRI:
            return PersonSerialization()

        if classIri == Vocabulary.FATHER_CLASS_IRI:
            return FatherSerialization()

        if classIri == Vocabulary.PARENT_CLASS_IRI:
            return ParentSerialization()

        if classIri == Vocabulary.MOTHER_CLASS_IRI:
            return MotherSerialization()

        if classIri == Vocabulary.WOMAN_CLASS_IRI:
            return WomanSerialization()

        if classIri == Vocabulary.DOGGY_CLASS_IRI:
            return DoggySerialization()

        if classIri == Vocabulary.CAT_CLASS_IRI:
            return CatSerialization()

        if classIri == Vocabulary.MEN_CLASS_IRI:
            return MenSerialization()

        if classIri == Vocabulary.CHILDLESSPERSON_CLASS_IRI:
            return ChildlessPersonSerialization()

        if classIri == Vocabulary.DOG_CLASS_IRI:
            return DogSerialization()

        if classIri == Vocabulary.PET_CLASS_IRI:
            return PetSerialization()

        return None

    def getSerializationInstance(self, ontology, instanceIri):
        classIri = self.getFirstIriObject(ontology, RDF.type, instanceIri)
        if classIri == Vocabulary.HUMAN_CLASS_IRI:
            return HumanSerialization()
        if classIri == Vocabulary.CHILD_CLASS_IRI:
            return ChildSerialization()
        if classIri == Vocabulary.PERSON_CLASS_IRI:
            return PersonSerialization()
        if classIri == Vocabulary.FATHER_CLASS_IRI:
            return FatherSerialization()
        if classIri == Vocabulary.PARENT_CLASS_IRI:
            return ParentSerialization()
        if classIri == Vocabulary.MOTHER_CLASS_IRI:
            return MotherSerialization()
        if classIri == Vocabulary.WOMAN_CLASS_IRI:
            return WomanSerialization()
        if classIri == Vocabulary.DOGGY_CLASS_IRI:
            return DoggySerialization()
        if classIri == Vocabulary.CAT_CLASS_IRI:
            return CatSerialization()
        if classIri == Vocabulary.MEN_CLASS_IRI:
            return MenSerialization()
        if classIri == Vocabulary.CHILDLESSPERSON_CLASS_IRI:
            return ChildlessPersonSerialization()
        if classIri == Vocabulary.DOG_CLASS_IRI:
            return DogSerialization()
        if classIri == Vocabulary.PET_CLASS_IRI:
            return PetSerialization()
        return None

    def getFirstIriObject(self,ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,URIRef):
                return object
        return None

