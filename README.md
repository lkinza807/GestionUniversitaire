Gestion Universitaire 

Description

Ce projet pédagogique manipule un fichier XML décrivant une université (départements, enseignants, cours, étudiants, inscriptions et notes). Il démontre deux étapes essentielles du traitement XML :

1. Validation du document XML contre un schéma XSD (structure, types, contraintes de valeurs).
2. Interrogation du document avec XQuery pour extraire des informations ciblées.

--
Structure du projet

```
tp1-xml/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── org/example/
        │       └── Main.java
        └── resources/
            ├── universite.xml
            └── universite.xsd
```

| Fichier | Rôle |
|---|---|
| `Main.java` | Point d'entrée : validation XSD + requêtes XQuery |
| `universite.xml` | Document XML contenant les données de l'université |
| `universite.xsd` | Schéma de validation du document XML |
| `pom.xml` | Configuration Maven et dépendances |

---

Fonctionnalités

Validation XML / XSD

Le programme valide `universite.xml` contre `universite.xsd`. Le schéma impose notamment :

- ✅ Un email au format `xxx@gmail.com`
- ✅ Une moyenne comprise entre 0 et 20
- ✅ Un nombre de crédits entre 0 et 10 par cours
- ✅ Les notes entre 0 et 20
- ✅ Les attributs obligatoires (`id`, `niveau`, `actif`, `semestre`, etc.)
- ✅ Une date au format `xs:date`

Exemple de sortie :

```
1. VALIDATION XML/XSD
✓ Le document XML est VALIDE selon le schéma XSD.
```

---

Requêtes XQuery

Sept requêtes sont exécutées sur le document :

| # | Requête | Description |
|---|---|---|
| **Q1** | Noms de tous les enseignants | `//enseignant/nom` |
| **Q2** | Étudiants actifs (`actif = 'true'`) | Filtre sur attribut |
| **Q3** | Étudiants `LSI3` avec moyenne > 15 | Filtre multi-critères |
| **Q4** | Cours du semestre `S5` | Filtre sur attribut |
| **Q5** | Nombre total d'étudiants | `count(//etudiant)` |
| **Q6** | Email du meilleur étudiant | `max()` + `for` |
| **Q7** | Notes obtenues au cours `C01` | Filtre sur `@coursId` |

Exemple de sortie :

```
>>> 2. INTERROGATION XQUERY <<<

[Q1] Noms de tous les enseignants :
   - ZAYANI
   - HAKIM
   - MOHAMED

[Q2] Étudiants actifs :
   - Ben Ali
   - Trabelsi
   - Kammoun

[Q6] Email de l'étudiant avec la meilleure moyenne :
   - salma.kammoun@gmail.com
```

---

Remarques pédagogiques

- Le fichier XML a été conçu pour être valide par rapport au XSD fourni.
- Le schéma XSD illustre plusieurs mécanismes : `xs:sequence`, `xs:restriction`, `xs:pattern`, `xs:minInclusive`, `xs:maxInclusive`, attributs `use="required"`, etc.
- Les requêtes XQuery couvrent les expressions `for`, `where`, `let`, `return`, `data()`, `count()`, `max()`.

---
