package model.jeu.dao
import coBDD
import jdbc.BDD
import model.item.Potion
import model.item.Qualite
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class PotionDAO (val bdd: BDD= coBDD){
    fun findAll(): MutableMap<String, Potion> {
        val result = mutableMapOf<String, Potion>()

        val sql = "SELECT * FROM Potion"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description= resultatRequete.getString("description")
                val soin= resultatRequete.getInt("soins")
                result.set(nom.lowercase(), Potion(id,nom,description,soin))
            }
        }
        requetePreparer.close()
        return result
    }
    fun findByNom(nomRechecher:String): MutableMap<String,Potion> {
        val result = mutableMapOf<String,Potion>()

        val sql = "SELECT * FROM Potion WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description= resultatRequete.getString("description")
                val soin= resultatRequete.getInt("soins")
                result.set(nom.lowercase(),Potion(id,nom,description,soin))
            }
        }
        requetePreparer.close()
        return result
    }
    fun findById(id:Int): Potion? {
        var result :Potion?=null
        val sql = "SELECT * FROM Potion WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description= resultatRequete.getString("description")
                val soin= resultatRequete.getInt("soins")
                result=Potion(id,nom,description,soin)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }


    fun save(unePotion: Potion): Potion? {
        val qualiteBdd=this.findByNom(unePotion.nom)
        if(qualiteBdd.isEmpty()) {


            val requetePreparer: PreparedStatement

            if (unePotion.id == null) {
                val sql =
                    "Insert Into Potion (nom,description,soin) values (?,?,?)"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                requetePreparer?.setString(1, unePotion.nom)
                requetePreparer?.setString(2, unePotion.description)
                requetePreparer?.setInt(3, unePotion.soin)
            } else {
                var sql = ""
                sql =
                    "Update  Potion set nom=?,description=?,soin=? where id=?"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

                requetePreparer?.setString(1, unePotion.nom)
                requetePreparer?.setString(2, unePotion.description)
                requetePreparer?.setInt(3, unePotion.soin)
                requetePreparer?.setInt(4, unePotion.id!!)
            }


            // Exécutez la requête d'insertion
            val nbLigneMaj = requetePreparer?.executeUpdate()
            // La méthode executeUpdate() retourne le nombre de lignes modifié par un insert, update ou delete sinon elle retourne 0 ou -1

            // Si l'insertion a réussi
            if (nbLigneMaj != null && nbLigneMaj > 0) {
                // Récupérez les clés générées (comme l'ID auto-incrémenté)
                val generatedKeys = requetePreparer.generatedKeys
                if (generatedKeys.next()) {
                    val id = generatedKeys.getInt(1) // Supposons que l'ID est la première col
                    unePotion.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                    return unePotion
                }
            }
            requetePreparer.close()
        }
        else{
            println("La qualite ${unePotion.nom} existe deja dans la bdd.")
        }
        return null
    }
    fun saveAll(lesPotion:Collection<Potion>):MutableMap<String,Potion>{
        var result= mutableMapOf<String,Potion>()
        for (unePotion in lesPotion){
            val potionSauvegarde=this.save(unePotion)
            if (potionSauvegarde!=null){
                result.set(potionSauvegarde.nom.lowercase(),potionSauvegarde)
            }
        }
        return result
    }
    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM Qualite WHERE id = ?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setInt(1, id)
        try {
            val nbLigneMaj = requetePreparer?.executeUpdate()
            requetePreparer.close()
            if(nbLigneMaj!=null && nbLigneMaj>0){
                return true
            }else{
                return false
            }
        } catch (erreur: SQLException) {
            println("Une erreur est survenue lors de la suppression de la qualité : ${erreur.message}")
            return false
        }
    }
}