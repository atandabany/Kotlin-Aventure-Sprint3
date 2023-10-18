package model.jeu.dao
import coBDD
import jdbc.BDD
import model.item.Arme

class ArmeDAO(val bdd: BDD=coBDD) {
    fun findAll(): MutableMap<String,Arme> {
        val result = mutableMapOf<String,Arme>()

        val sql = "SELECT * FROM arme"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val id_qualite= resultatRequete.getInt("qualite")
                val id_type= resultatRequete.getString("valeurMax")
                result.set(nom.lowercase(),Arme(id,nom,id_qualite,id_type))
            }
        }
        requetePreparer.close()
        return result
    }
}