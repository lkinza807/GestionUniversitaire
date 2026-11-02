package org.example;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import net.sf.saxon.s9api.*;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final String XML_FILE = "src/main/resources/universite.xml";
    private static final String XSD_FILE = "src/main/resources/universite.xsd";

    public static void main(String[] args) {
        System.out.println(" TP1 : Gestion Universitaire - XML/XQuery");

        validerXMLavecXSD();
        interrogerAvecXQuery();
    }

    private static void validerXMLavecXSD() {
        System.out.println("1. VALIDATION XML/XSD ");
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(XSD_FILE));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(XML_FILE)));
            System.out.println("✓ Le document XML est VALIDE selon le schéma XSD.");
        } catch (SAXException e) {
            System.out.println("✗ Document XML INVALIDE : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("✗ Erreur de lecture : " + e.getMessage());
        }
    }

    private static void interrogerAvecXQuery() {
        System.out.println(">>> 2. INTERROGATION XQUERY <<<");
        try {
            Processor processor = new Processor(false);
            net.sf.saxon.s9api.DocumentBuilder builder = processor.newDocumentBuilder();
            XdmNode doc = builder.build(new File(XML_FILE));

            XQueryCompiler compiler = processor.newXQueryCompiler();

            System.out.println("\n[Q1] Noms de tous les enseignants :");
            executerXQuery(compiler, doc,
                    "for $e in //enseignant return data($e/nom)");

            System.out.println("\n[Q2] Étudiants actifs :");
            executerXQuery(compiler, doc,
                    "for $e in //etudiant where $e/@actif = 'true' return data($e/nom)");

            System.out.println("\n[Q3] Étudiants LSI3 avec moyenne > 15 :");
            executerXQuery(compiler, doc,
                    "for $e in //etudiant where $e/@niveau = 'LSI3' and $e/moyenne > 15 return data($e/nom)");

            System.out.println("\n[Q4] Cours du semestre S5 :");
            executerXQuery(compiler, doc,
                    "for $c in //cours where $c/@semestre = 'S5' return data($c/intitule)");

            System.out.println("\n[Q5] Nombre total d'étudiants :");
            executerXQuery(compiler, doc,
                    "count(//etudiant)");

            System.out.println("\n[Q6] Email de l'étudiant avec la meilleure moyenne :");
            executerXQuery(compiler, doc,
                    "let $max := max(//etudiant/moyenne) " +
                            "for $e in //etudiant where $e/moyenne = $max return data($e/email)");

            System.out.println("\n[Q7] Notes obtenues au cours C01 :");
            executerXQuery(compiler, doc,
                    "for $i in //inscription where $i/@coursId = 'C01' return data($i/note)");

        } catch (Exception e) {
            System.out.println("✗ Erreur XQuery : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executerXQuery(XQueryCompiler compiler, XdmNode doc, String query) {
        try {
            XQueryExecutable exp = compiler.compile(query);
            XQueryEvaluator evaluator = exp.load();
            evaluator.setContextItem(doc);

            XdmValue result = evaluator.evaluate();
            for (XdmItem item : result) {
                System.out.println("   - " + item.getStringValue());
            }
        } catch (SaxonApiException e) {
            System.out.println("   ✗ Erreur dans la requête : " + e.getMessage());
        }
    }
}