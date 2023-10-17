package model.jeu.generateur
import java.nio.file.Paths
import java.nio.file.Files
import model.item.TypeArmure

class GenerateurTypeArmure (val cheminFichier : String) {
    fun generer(): MutableMap<String, TypeArmure> {
        val mapObjets = mutableMapOf<String, TypeArmure>()

        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val objet = TypeArmure(
                nom = ligneObjet[0].toString(),
                bonusType = ligneObjet[1].toInt()
            )
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}