import model.item.Item
import model.personnage.Personnage
import java.nio.file.Files
import java.nio.file.Paths
import Potion


class GenerateurMonstres (val cheminFichier : String) {
    fun generer(): MutableMap<String, Personnage> {
        val mapObjets = mutableMapOf<String, Personnage>()
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)
        val inventaire = mutableListOf<Item>()
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val armeEquipe = arme[ligneObjet[7].lowercase()]!!
            val armureEquipe = armure[ligneObjet[8].lowercase()]!!

            val listeNomArme= ligneObjet[9].lowercase().split(".")
            for(unNomArme in listeNomArme){
                inventaire.add(arme[unNomArme]!!)
            }
            val listeNomArmure= ligneObjet[10].lowercase().split(".")
            for(unNomArmure in listeNomArmure) {
                inventaire.add(armure[unNomArmure]!!)
            }
            val listeNomBombe= ligneObjet[11].lowercase().split(".")
            for(unNomBombe in listeNomBombe) {
                inventaire.add(bombes[unNomBombe]!!)
            }
            val listeNomPotion= ligneObjet[12].lowercase().split(".")
            for(unNomPotion in listeNomPotion) {
                inventaire.add(potions[unNomPotion]!!)
            }
            val objet = Personnage(
                nom = ligneObjet[0].toString(),
                pointDeVie = ligneObjet[1].toInt(),
                pointDeVieMax = ligneObjet[2].toInt(),
                attaque = ligneObjet[3].toInt(),
                defense = ligneObjet[4].toInt(),
                endurance = ligneObjet[5].toInt(),
                vitesse = ligneObjet[6].toInt(),
                armeEquipee = armeEquipe,
                armureEquipee = armureEquipe,
                inventaire = inventaire
            )
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}