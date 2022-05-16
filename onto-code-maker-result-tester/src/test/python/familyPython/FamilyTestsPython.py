import unittest
from rdflib import Graph, URIRef
from rdflib.namespace import RDF
import sys
import os
from Vocabulary import Vocabulary
from FamilyFactory import FamilyFactory
from entities.EquivalenceHumanPerson import EquivalenceHumanPerson
from entities.EquivalencePetUnionOfCatDog import EquivalencePetUnionOfCatDog
from entities.EquivalenceIntersectionOfParentWomanMother import EquivalenceIntersectionOfParentWomanMother
from entities.EquivalenceChildlessPersonComplementOfParent import EquivalenceChildlessPersonComplementOfParent
#sys.path.append(os.path.dirname(os.path.realpath(__file__)) + "/entities")

#current = os.path.dirname(os.path.realpath(__file__)) 
#import sys, os
#sys.path.extend([f'./{name}' for name in os.listdir(".") if os.path.isdir(name)])
#import sys
#sys.path.append( './entities' )
from entities.Human import Human

MartinHuman = "http://example.com/Martin"
MatusPerson = "http://example.com/Matus"
LukasPerson = "http://example.com/Lukas"

JurajMen = "http://example.com/Juraj"
MarekMen = "http://example.com/Marek"
TomasMen = "http://example.com/Tomas"
JanMen = "http://example.com/Jan"
FilipMen = "http://example.com/Filip"
JakubMen = "http://example.com/Jakub"
RomanMen = "http://example.com/Roman"

JanChild = "http://example.com/child/Janko"
TomasChild = "http://example.com/child/Tomas"
FilipChild = "http://example.com/child/Filip"
MatusChild = "http://example.com/child/Matus"
FelixParent = "http://example.com/parent/Felix"
MarekParent = "http://example.com/parent/Marek"
LukasParent = "http://example.com/parent/Lukas"

JurajChildlessPerson = "http://example.com/childlessperson/Juraj"

FelixFather = "http://example.com/father/Felix"
MonikaMother = "http://example.com/mother/Monika"


MonikaWoman = "http://example.com/Monika"
JanaWoman = "http://example.com/Jana"
VeronikaWoman = "http://example.com/Veronika"

TomPet = "http://example.com/pet/Tom"

MnaukaCat = "http://example.com/Mnauka"
TomCat = "http://example.com/Tom"

HauDog = "http://example.com/HauDog"
TomasDoggy = "http://example.com/doggy/Tomas"


class TestFamily(unittest.TestCase):
    MartinHuman = "http://example.com/Martin"

    def setUp(self):
        self.graph = Graph()
        self.graph.parse("family.owl")
        self.factory = FamilyFactory(self.graph)

    #1
    def testAllInstancesWithoutInstance(self):
        empty = set()
        self.assertEqual( self.factory.getAllHumanInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllPersonInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllMenInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllWomanInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllCatInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllDogInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllDoggyInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllChildInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllPetInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllParentInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllFatherInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllMotherInstancesFromModel() , empty, "Class is not empty.")
        self.assertEqual( self.factory.getAllChildlessPersonInstancesFromModel() , empty, "Class is not empty.")

    #2
    def testInstanceWhenModelIsEmpty(self):
        self.assertIsNone(self.factory.getMenFromModel(MartinHuman),"Get men instance is not null.")

    #3
    def testCreateInstance(self):
        self.assertEqual(str(self.factory.createHuman(MartinHuman).getIri()),MartinHuman,"Problem create Instance of Human Class")
        self.assertEqual(str(self.factory.createCat(TomCat).getIri()),TomCat,"Problem create Instance of Cat Class")
        self.assertEqual(str(self.factory.createDog(HauDog).getIri()),HauDog,"Problem create Instance of Dog Class")
        self.assertEqual(str(self.factory.createChild(JanChild).getIri()),JanChild,"Problem create Instance of Child Class")
        self.assertEqual(str(self.factory.createChildlessPerson(JurajChildlessPerson).getIri()),JurajChildlessPerson,"Problem create Instance of JurajChildlessPerson Class")
        self.assertEqual(str(self.factory.createFather(FelixFather).getIri()),FelixFather,"Problem create Instance of Father Class")
        self.assertEqual(str(self.factory.createDoggy(TomasDoggy).getIri()),TomasDoggy,"Problem create Instance of Doggy Class")
        self.assertEqual(str(self.factory.createParent(FelixParent).getIri()),FelixParent,"Problem create Instance of Parent Class")
        self.assertEqual(str(self.factory.createMother(MonikaMother).getIri()),MonikaMother,"Problem create Instance of Mother Class")
        self.assertEqual(str(self.factory.createMen(TomasMen).getIri()),TomasMen,"Problem create Instance of Men Class")
        self.assertEqual(str(self.factory.createWoman(MonikaWoman).getIri()),MonikaWoman,"Problem create Instance of MonikaWoman Class")
        self.assertEqual(str(self.factory.createWoman(LukasPerson).getIri()),LukasPerson,"Problem create Instance of Person Class")
        self.assertEqual(str(self.factory.createWoman(TomPet).getIri()),TomPet,"Problem create Instance of Pet Class")

    #4
    def testRemoveNonExistInstance(self):
        self.factory.removeMenFromModel(MartinHuman)
        self.assertIsNone(self.factory.getMenFromModel(MartinHuman),"Get men instance is not null.")

    #5
    def testAddToModel(self):
        startSize = len(self.graph)
        h = self.factory.createHuman(MartinHuman)
        self.factory.addToModel(h)
        #self.assertEqual(len(self.graph),startSize+1,"Problem add instance to model.")
        self.assertTrue((URIRef(MartinHuman),RDF.type,Vocabulary.HUMAN_CLASS_IRI) in self.graph,"Problem add instance to model 2.")
        h = self.factory.getHumanFromModel(MartinHuman)
        self.assertIsNotNone(h,"Problem get instance from model.")

    #6
    def testGetAllFromModel(self):
        h = self.factory.createHuman(MartinHuman)
        self.factory.addToModel(h)
        hs = self.factory.getAllHumanInstancesFromModel()
        self.assertIsNotNone(hs,"Problem get instance from model.")
        self.assertEqual(len(hs),1,"Problem get all Human instances from model.")

    #7
    def testRemoveFromModel(self):
        h = self.factory.createHuman(MartinHuman)
        self.factory.addToModel(h)
        self.factory.removeHumanFromModel(MartinHuman)
        self.assertFalse((URIRef(MartinHuman),RDF.type,Vocabulary.HUMAN_CLASS_IRI) in self.graph,"Problem remove instance from model.")
        hs = self.factory.getHumanFromModel(MartinHuman)
        self.assertIsNone(hs,"Problem remove instance from model.")

    #8
    def testGetAllFromModel(self):
        h = self.factory.createMen(MarekMen)
        self.factory.addToModel(h)
        h2 = self.factory.createMen(JurajMen)
        self.factory.addToModel(h2)
        hs = self.factory.getAllMenInstancesFromModel()
        self.assertIsNotNone(hs,"Problem get instance from model.")
        self.assertEqual(len(hs),2,"Problem get all Human instances from model.")

    #9
    def testSimpleEquivalentOfToModel(self):
        p = self.factory.createPerson(MatusPerson)
        self.factory.addToModel(p)
        hs = self.factory.getPersonFromModel(MatusPerson)
        self.assertIsNotNone(hs,"Problem get equivalent instance from model.")
        self.assertEqual(str(hs.getIri()),MatusPerson,"Problem get instance from model.")
        self.assertTrue(isinstance(hs,EquivalenceHumanPerson),"Person is not equivalence with human")
    
    #10
    def testSimpleSubClassOfToModel(self):
        h = self.factory.createMen(MarekMen)
        self.factory.addToModel(h)
        h2 = self.factory.createMen(JurajMen)
        self.factory.addToModel(h2)
        w = self.factory.createWoman(MonikaWoman)
        self.factory.addToModel(w)
        hs = self.factory.getAllMenInstancesFromModel()
        woman = self.factory.getWomanFromModel(MonikaWoman)
        self.assertIsNotNone(woman,"Woman is null")
        self.assertTrue(isinstance(woman,Human),"Woman is not instance of Human")
        for men in hs:
            self.assertTrue(isinstance(men,Human),"Men is not instance of Human")

    #11
    def testClassDefinedEquivalentOfRestriction(self):
        d = self.factory.createDoggy(TomasDoggy)
        self.factory.addToModel(d)
        doggy = self.factory.getDoggyFromModel(TomasDoggy)
        self.assertIsNotNone(doggy,"Doggy is null")
        self.assertEqual(str(doggy.getIri()),TomasDoggy,"Problem get instance from model.")
        self.factory.removeDoggyFromModel(TomasDoggy)
        hs = self.factory.getDoggyFromModel(TomasDoggy)
        self.assertIsNone(hs,"Problem remove instance from model.")

    #12
    def testClassDefinedEquivalentOfUnion(self):
        p = self.factory.createPet(TomPet)
        self.factory.addToModel(p)
        pet = self.factory.getPetFromModel(TomPet)
        self.assertIsNotNone(pet,"Pet is null")
        self.assertEqual(str(pet.getIri()),TomPet,"Problem get instance from model.")
        self.assertTrue(isinstance(pet,EquivalencePetUnionOfCatDog),"Pet is not instance of EquivalencePetUnionOfCatDog")
        self.factory.removePetFromModel(TomPet)
        hs = self.factory.getPetFromModel(TomPet)
        self.assertIsNone(hs,"Problem remove instance from model.")
    
    #13
    def testClassDefinedEquivalentOfIntersection(self):
        p = self.factory.createMother(MonikaMother)
        self.factory.addToModel(p)
        mother = self.factory.getMotherFromModel(MonikaMother)
        self.assertIsNotNone(mother,"Mother is null")
        self.assertEqual(str(mother.getIri()),MonikaMother,"Problem get instance from model.")
        self.assertTrue(isinstance(mother,EquivalenceIntersectionOfParentWomanMother),"Mother is not instance of EquivalenceIntersectionOfParentWomanMother")
        self.factory.removeMotherFromModel(MonikaMother)
        hs = self.factory.getMotherFromModel(MonikaMother)
        self.assertIsNone(hs,"Problem remove instance from model.")


    #14
    def testClassDefinedEquivalentOfComplement(self):
        p = self.factory.createChildlessPerson(JurajChildlessPerson)
        self.factory.addToModel(p)
        childless = self.factory.getChildlessPersonFromModel(JurajChildlessPerson)
        self.assertIsNotNone(childless,"Mother is null")
        self.assertEqual(str(childless.getIri()),JurajChildlessPerson,"Problem get instance from model.")
        self.assertTrue(isinstance(childless,EquivalenceChildlessPersonComplementOfParent),"ChildlessPerson is not instance of EquivalenceChildlessPersonComplementOfParent")
        self.factory.removeChildlessPersonFromModel(JurajChildlessPerson)
        hs = self.factory.getChildlessPersonFromModel(JurajChildlessPerson)
        self.assertIsNone(hs,"Problem remove instance from model.")


    #
    #   Properties Test
    #


    #15
    def testDataTypeProperties(self):
        h = self.factory.createHuman(MartinHuman)
        h.setHasAge(35)
        h.addHasLuckyNumbers(42)
        h.addHasLuckyNumbers(55)
        self.factory.addToModel(h)
        resultModel = Graph()
        resultModel += self.graph.triples((URIRef(MartinHuman),None,None))
        self.assertEqual(len(resultModel),3,"Problem add instance to model 2.")
        
        human = self.factory.getHumanFromModel(MartinHuman)
        self.assertIsNotNone(human,"Human is null")
        self.assertEqual(human.getHasAge(),'35',"Has age properties is wrong.")
        self.assertEqual(len(human.getHasLuckyNumbers()),2,"Has lucky numbers property is empty.")
        self.assertTrue('42' in human.getHasLuckyNumbers() and '55' in human.getHasLuckyNumbers(),"Values in hasLuckyNumbers property are not correct.")
        self.factory.removeHumanFromModel(MartinHuman)

    #16
    def testSetProperties(self):
        c = self.factory.createCat(MnaukaCat)
        self.factory.addToModel(c)
        h = self.factory.createHuman(MartinHuman)
        h.setHasAge(35)
        h.addHasCat(c)
        self.factory.addToModel(h)
        resultModel = Graph()
        resultModel += self.graph.triples((URIRef(MartinHuman),None,None))
        self.assertEqual(len(resultModel),3,"Problem add instance to model 2.")

    #17
    def testGetProperties(self):
        c = self.factory.createCat(MnaukaCat)
        self.factory.addToModel(c)
        h = self.factory.createHuman(MartinHuman)
        h.setHasAge(35)
        h.addHasCat(c)
        self.factory.addToModel(h)
        human = self.factory.getHumanFromModel(MartinHuman)
        self.assertIsNotNone(human,"Human is null")
        self.assertEqual(human.getHasAge(),'35',"Has age properties is wrong.")
        self.assertEqual(len(human.getHasCat()),1,"Has cat property is empty.")
        li = list(human.getHasCat())[0]
        self.assertEqual(str(li.getIri()),MnaukaCat,"Has cat properties is wrong.")

    #18
    def testSetGetSubClassProperties(self):
        c = self.factory.createCat(MnaukaCat)
        self.factory.addToModel(c)
        m = self.factory.createMen(JanMen)
        m.setHasAge(45)
        m.addHasCat(c)
        self.factory.addToModel(m)
        men = self.factory.getMenFromModel(JanMen)
        self.assertIsNotNone(men,"Human is null")
        self.assertEqual(men.getHasAge(),'45',"Has age properties is wrong.")
        self.assertEqual(len(men.getHasCat()),1,"Has cat property is empty.")
        li = list(men.getHasCat())[0]
        self.assertEqual(str(li.getIri()),MnaukaCat,"Has cat properties is wrong.")

    #19
    def testSetPropertiesOfEquivalentClass(self):
        c = self.factory.createCat(MnaukaCat)
        self.factory.addToModel(c)
        m = self.factory.createPerson(LukasPerson)
        m.setHasAge(20)
        m.addHasCat(c)
        self.factory.addToModel(m)
        person = self.factory.getPersonFromModel(LukasPerson)
        self.assertIsNotNone(person,"Human is null")
        self.assertEqual(person.getHasAge(),'20',"Has age properties is wrong.")
        self.assertEqual(len(person.getHasCat()),1,"Has cat property is empty.")
        li = list(person.getHasCat())[0]
        self.assertEqual(str(li.getIri()),MnaukaCat,"Has cat properties is wrong.")

    #20
    def testFunctionalProperty(self):
        d = self.factory.createDog(HauDog)
        self.factory.addToModel(d)
        m = self.factory.createMen(TomasMen)
        m.setHasAge(38)
        m.setHasDog(d)
        self.factory.addToModel(m)
        men = self.factory.getMenFromModel(TomasMen)
        self.assertIsNotNone(men,"Human is null")
        self.assertEqual(men.getHasAge(),'38',"Has age properties is wrong.")
        self.assertEqual(str(men.getHasDog().getIri()),HauDog,"Has dog properties is wrong.")

    #21
    def testInverseFunctionalProperty(self):
        ch = self.factory.createChild(JanChild)
        self.factory.addToModel(ch)
        chT = self.factory.createChild(TomasChild)
        self.factory.addToModel(chT)
        p = self.factory.createParent(FelixParent)
        p.addHasChild(ch)
        p.addHasChild(chT)
        self.factory.addToModel(p)

        p2 = self.factory.getParentFromModel(FelixParent)
        self.assertIsNotNone(p2,"Parent is null")
        self.assertEqual(len(p2.getHasChild()),2,"Has child property is empty.")
        li = list(p2.getHasChild())[0]
        self.assertTrue(str(li.getIri()) == TomasChild or str(li.getIri()) == JanChild ,"Has child properties is wrong.")

        tc = self.factory.getChildFromModel(TomasChild)
        self.assertIsNotNone(tc,"Child is null")
        self.assertIsNotNone(tc.getParent(),"Parent in child property is null.")
        self.assertEqual(str(tc.getParent().getIri()),FelixParent,"Parent inverse property is wrong.")

    #22
    def testInverseFunctionalAndFunctionalProperty(self):
        w = self.factory.createWoman(JanaWoman)
        self.factory.addToModel(w)
        m = self.factory.createMen(FilipMen)
        m.setHasAge(38)
        m.setHasWife(w)
        self.factory.addToModel(m)
        t = self.factory.getMenFromModel(FilipMen)
        self.assertIsNotNone(t,"Child is null")
        self.assertIsNotNone(t.getHasWife(),"Has wife in child property is null.")
        self.assertEqual(str(t.getHasWife().getIri()),JanaWoman,"Has wife inverse functional property is wrong.")

        w2 = self.factory.getWomanFromModel(JanaWoman)
        self.assertIsNotNone(w2,"Woman is null")
        self.assertIsNotNone(w2.getMen(),"Has men in child property is null.")
        self.assertEqual(str(w2.getMen().getIri()),FilipMen,"Has men inverse functional property is wrong.")

    #23
    def testSetGetEquivalentProperty(self):
        d = self.factory.createDog(HauDog)
        self.factory.addToModel(d)
        m = self.factory.createMen(RomanMen)
        m.setHasAge(38)
        m.setHasDog(d)
        self.factory.addToModel(m)
        m = self.factory.getMenFromModel(RomanMen)
        self.assertIsNotNone(m,"Men is null")
        self.assertIsNotNone(m.getHasDog(),"Has dog properties is null.")
        self.assertEqual(len(m.getHasDogEq()),1,"Equivalent Has dog properties is not set.")
        li = list(m.getHasDogEq())[0]
        self.assertEqual(str(li.getIri()),HauDog,"Has dog eq properties is wrong.")

    #24
    def testSubPropertyOfProperty(self):
        ch = self.factory.createChild(FilipChild)
        p = self.factory.createParent(MarekParent)
        self.factory.addToModel(p)
        ch.addHasParent(p)
        self.factory.addToModel(ch)

        fc = self.factory.getChildFromModel(FilipChild)
        self.assertIsNotNone(fc,"Men is null")
        self.assertEqual(len(fc.getHasParent()),1," Has parent properties is not set.")
        self.assertEqual(len(fc.getHasFather()),0," Has father properties is not set.")
        li = list(fc.getHasParent())[0]
        self.assertEqual(str(li.getIri()),MarekParent,"Has parent properties is wrong.")

    #25
    def testSetSubPropertyOfProperty(self):
        ch = self.factory.createChild(MatusChild)
        p = self.factory.createParent(LukasParent)
        self.factory.addToModel(p)
        ch.addHasFather(p)
        self.factory.addToModel(ch)

        fc = self.factory.getChildFromModel(MatusChild)
        self.assertIsNotNone(fc,"Child is null")
        self.assertEqual(len(fc.getHasParent()),1," Has parent properties is not set.")
        self.assertEqual(len(fc.getHasFather()),1," Has father properties is not set.")
        li = list(fc.getHasParent())[0]
        self.assertEqual(str(li.getIri()),LukasParent,"Has parent properties is wrong.")

        pl = self.factory.getParentFromModel(LukasParent)
        self.assertIsNotNone(pl,"Parent is null")
        self.assertEqual(len(pl.getHasChild()),0,"Has child property is not empty.")



    #26
    def testInversePropertyOfProperty(self):
        w = self.factory.createWoman(VeronikaWoman)
        self.factory.addToModel(w)
        m = self.factory.createMen(JakubMen)
        m.setHasWife(w)
        self.factory.addToModel(m)

        w2 = self.factory.getWomanFromModel(VeronikaWoman)
        self.assertIsNotNone(w2,"Woman is null")
        self.assertEqual(len(w2.getHasHusband()),1,"Has husband property in woman class is null.")
        li = list(w2.getHasHusband())[0]
        self.assertEqual(str(li.getIri()),JakubMen,"Has husband properties is wrong.")



if __name__ == '__main__':
    unittest.main()