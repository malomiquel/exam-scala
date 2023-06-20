# Examen Scala

Ce projet Scala permet de récupérer les pages Wikipedia correspondant à un mot-clé donné et d'afficher certaines statistiques basées sur ces pages.

## Classes et Fonctions

### `Config`

`Config` est une classe de données qui stocke les paramètres de configuration de l'application.

- `limit` est le nombre maximum de pages à renvoyer, par défaut il est réglé sur 10.
- `keyword` est le mot-clé à rechercher dans l'API Wikipedia.

### `WikiPage`

`WikiPage` est une classe de données qui stocke les informations d'une page Wikipedia.

- `title` est le titre de la page.
- `words` est le nombre de mots de la page.

### `HttpUtils`

`HttpUtils` est une interface qui définit une méthode pour créer une requête HTTP.

- `parse` est une méthode qui prend une URL et renvoie une `HttpRequest`.

### `Main`

`Main` est la classe principale qui gère l'exécution de l'application.

- `parseArguments` analyse les arguments de ligne de commande et renvoie un `Option[Config]`.
- `formatUrl` formate l'URL de l'API Wikipedia en utilisant le mot-clé et la limite spécifiés.
- `getPages` fait une requête HTTP à l'URL spécifiée et renvoie le corps de la réponse en cas de succès ou le code d'erreur en cas d'échec.
- `parseJson` prend une chaîne JSON brute et la convertit en une séquence de `WikiPage`.
- `totalWords` calcule le nombre total de mots dans une séquence de `WikiPage`.
- `displayPages` affiche les statistiques basées sur une séquence de `WikiPage`.
- `run` est la fonction principale qui exécute l'application en utilisant les paramètres de configuration spécifiés.

## Comment utiliser

Lancer le programme avec les arguments `-l` (limite, optionnel) et `-k` (mot-clé, obligatoire).

Par exemple :

```
run -l 20 -k scala
```

Ceci fera une recherche dans l'API Wikipedia pour les pages qui contiennent le mot "scala" et affichera des statistiques pour les 20 premières pages renvoyées.