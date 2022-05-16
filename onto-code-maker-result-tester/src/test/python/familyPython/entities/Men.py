from rdflib import URIRef
from Vocabulary import Vocabulary
 
from .Human import Human


##
#  This is the class representing the Men(http://www.ontocodemaker.org/Family#Men) class from ontology
#  This class is subclass of  Human
#
#  Men
#  This is men
#  Author of class Tomas
#   This class is disjoint with classes:
#        Woman(http://www.ontocodemaker.org/Family#Woman)
#
#   Generated by OntoCodeMaker
##

class Men ( Human):


    CLASS_IRI = Vocabulary.MEN_CLASS_IRI
    def __init__(self,iri):
        super().__init__(iri)

        # Property http://www.ontocodemaker.org/Family#hasWife 2
        self.hasWife  = None 

        # Property http://www.ontocodemaker.org/Family#hasSpouse 2
        # The property is SubProperty of  hasWife
        self.hasSpouse  = set() 

    def getClassIRI(self):
        return Men.CLASS_IRI


    def setHasWife(self, hasWife):
        self.hasWife = hasWife


    def getHasWife(self):
        return self.hasWife

    def addHasSpouse(self, hasSpouse):
        self.hasSpouse.add(hasSpouse)
        if not self.hasWife == hasSpouse:
            self.hasWife = hasSpouse


    def getHasSpouse(self):
        return self.hasSpouse

