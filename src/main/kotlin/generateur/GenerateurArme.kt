package generateur
import model.item.Arme
import model.item.Qualite
import qualites
import typeArme
import java.nio.file.Paths
import java.nio.file.Files
class GenerateurArme(val cheminFichier: String) {
    fun generer(): MutableMap<String, Arme> {
        val mapObjets = mutableMapOf<String, Arme>()
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val qualite:Qualite=qualites[ligneObjet[3].lowercase()]!!
            val type=typeArme[ligneObjet[2].lowercase()]!!
            val objet = Arme(nom = ligneObjet[0], description = ligneObjet[1], type = type, qualite = qualite)
            mapObjets[cle] = objet
        }
        return mapObjets

    }
}