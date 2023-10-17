package model.jeu.generateur
import model.item.Armure
import model.item.Qualite
import qualites
import typeArmure
import java.lang.reflect.Type
import java.nio.file.Paths
import java.nio.file.Files
class GenerateurArmure (val cheminFichier : String) {
    fun generer(): MutableMap<String, Armure> {
        val mapObjets = mutableMapOf<String, Armure>()
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val qualite:Qualite = qualites[ligneObjet[2].lowercase()]!!
            val type= typeArmure[ligneObjet[3].lowercase()]!!
            val objet = Armure(
                nom = ligneObjet[0].toString(),
                description = ligneObjet[1].toString(),
                qualite = qualite,
                typeArmure = type
            )
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}