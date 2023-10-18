package model.jeu.dao
import coBDD
import jdbc.BDD
import model.item.TypeArmure
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement


class TypeArmureDAO (val bdd: BDD= coBDD) {
    /**
     * Recherche et retourne toutes les types d'armure de la base de données.
     *
     * @return Une liste de tous les types d'armure trouvées.
     */
    fun findAll(): MutableMap<String, TypeArmure> {
        val result = mutableMapOf<String,TypeArmure>()

        val sql = "SELECT * FROM TypeArmure"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id=resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonusType=resultatRequete.getInt("bonusType")
                result.set(nom.lowercase(),TypeArmure(id,nom,bonusType))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne un type d'amure par nom (retourne la première correspondance).
     *
     * @param nomRechecher Le nom à rechercher.
     * @return Le premier type d'armure correspondant au nom donné, ou null si aucun n'est trouvé.
     */
    fun findByNom(nomRechecher:String): MutableMap<String, TypeArmure> {
        val result = mutableMapOf<String, TypeArmure>()

        val sql = "SELECT * FROM TypeArmure WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id=resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonusType=resultatRequete.getInt("bonusType")
                result.set(nom.lowercase(),TypeArmure(id,nom,bonusType))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne un type d'armure par nom (retourne la première correspondance).
     *
     * @param Int L'id à rechercher.
     * @return Le premier type d'armure correspondant au nom donné, ou null si aucun n'est trouvé.
     */
    fun findById(id:Int): TypeArmure? {
        var result : TypeArmure?=null
        val sql = "SELECT * FROM TypeArmure WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id=resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonusType=resultatRequete.getInt("bonusType")
                result= TypeArmure(id,nom,bonusType)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Sauvegarde un type d'amure dans la base de données.
     *
     * @param unTypeArmure L'objet TypeArmure à sauvegarder.
     * @return L'objet TypeArmure sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(unTypeArmure: TypeArmure): TypeArmure? {
        val typeArmureBdd=this.findByNom(unTypeArmure.nom)
        if(typeArmureBdd.isEmpty()) {


            val requetePreparer: PreparedStatement

            if (unTypeArmure.id == null) {
                val sql =
                    "Insert Into TypeArmure (nom,bonusType) values (?,?)"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                requetePreparer?.setString(1, unTypeArmure.nom)
                requetePreparer?.setInt(2, unTypeArmure.bonusType)
            } else {
                var sql = ""
                sql =
                    "Update  TypeArmure set nom=?,bonusType=? where id=?"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

                requetePreparer?.setString(1, unTypeArmure.nom)
                requetePreparer?.setInt(2, unTypeArmure.bonusType)
                requetePreparer?.setInt(3, unTypeArmure.id!!)
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
                    unTypeArmure.id = id // Mettez à jour l'ID de l'objet TypeArmure avec la valeur générée
                    return unTypeArmure
                }
            }
            requetePreparer.close()
        }
        else{
            println("Le type d'armure ${unTypeArmure.nom} existe deja dans la bdd.")
        }
        return null
    }

    /**
     * Sauvegarde toutes les types d'armure dans la liste dans la base de données.
     *
     * @param lesTypesArmure La liste des objets TypeArmure à sauvegarder.
     * @return Une liste des objets TypeArmure sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesTypesArmure:Collection<TypeArmure>):MutableMap<String, TypeArmure>{
        var result= mutableMapOf<String, TypeArmure>()
        for (unTypeArmure in lesTypesArmure){
            val typeArmureSauvegarde=this.save(unTypeArmure)
            if (typeArmureSauvegarde!=null){
                result.set(typeArmureSauvegarde.nom.lowercase(),typeArmureSauvegarde)
            }
        }
        return result
    }

    /**
     * Supprime un type d'armure de la base de données en fonction de son ID.
     *
     * @param id L'ID du type d'armure à supprimer.
     * @return `true` si le type d'armure a été supprimé avec succès, sinon `false`.
     */
    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM TypeArmure WHERE id = ?"
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
            println("Une erreur est survenue lors de la suppression du type d'armure : ${erreur.message}")
            return false
        }
    }
}