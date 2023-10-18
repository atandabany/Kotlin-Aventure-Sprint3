package model.jeu.dao
import model.item.TypeArme
import coBDD
import jdbc.BDD
import java.lang.reflect.Type
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement


class TypeArmeDAO(val bdd: BDD =coBDD) {

    /**
     * Recherche et retourne toutes les typeArme de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String,TypeArme> {
        val result = mutableMapOf<String,TypeArme>()

        val sql = "SELECT * FROM TypeArme"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val nombreDes= resultatRequete.getString("nombreDes")
                val valeurMax= resultatRequete.getInt("valeurMax")
                val multiplicateurCritique= resultatRequete.getInt("multicateurCritique")
                val activationCritique= resultatRequete.getInt("activationCritique")
                result.set(nom.lowercase(),TypeArme(id,nom,nombreDes,valeurMax,multiplicateurCritique,activationCritique))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne un TypeArme par nom (retourne la première correspondance).
     *
     * @param nomRechecher Le nom à rechercher.
     * @return La première TypeArme correspondant au nom donné, ou null si aucune n'est trouvée.
     */
    fun findByNom(nomRechecher:String): MutableMap<String, TypeArme> {
        val result = mutableMapOf<String, TypeArme>()

        val sql = "SELECT * FROM TypeArme WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val nombreDes= resultatRequete.getString("nombreDes")
                val valeurMax= resultatRequete.getInt("valeurMax")
                val multiplicateurCritique= resultatRequete.getInt("multicateurCritique")
                val activationCritique= resultatRequete.getInt("activationCritique")
                result.set(nom.lowercase(), TypeArme(id,nom,nombreDes,valeurMax,multiplicateurCritique,activationCritique ))
            }
        }
        requetePreparer.close()
        return result
    }

    fun findById(id:Int): TypeArme? {
        var result :TypeArme?=null
        val sql = "SELECT * FROM Qualite WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val nombreDes= resultatRequete.getString("nombreDes")
                val valeurMax= resultatRequete.getInt("valeurMax")
                val multiplicateurCritique= resultatRequete.getInt("multicateurCritique")
                val activationCritique= resultatRequete.getInt("activationCritique")
                result=TypeArme(id,nom,nombreDes,valeurMax,multiplicateurCritique,activationCritique)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Sauvegarde un typearmedans la base de données.
     *
     * @param unTypeArme L'objet TypeArme à sauvegarder.
     * @return L'objet TypeArme sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(unTypeArme: TypeArme): TypeArme? {
        val typeArmebdd=this.findByNom(unTypeArme.nom.toString())
        if(typeArmebdd.isEmpty()) {


            val requetePreparer: PreparedStatement

            if (unTypeArme.id == null) {
                val sql =
                    "Insert Into TypeArme (nom,nombreDes,valeurDeMax,multicateurCritique,ActivationCritique) values (?,?,?)"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                requetePreparer?.setString(1, unTypeArme.nom)
                requetePreparer?.setString(2, unTypeArme.nombreDes)
                requetePreparer?.setInt(3, unTypeArme.valeurDeMax)
                requetePreparer?.setInt(4, unTypeArme.multiplicateurCritique)
                requetePreparer?.setInt(5, unTypeArme.activationCritique)
            } else {
                var sql = ""
                sql =
                    "Update  TypeArme set nom=?, nombreDes=?, valeurDeMax=?, multiplicateurCritique=?, activationCritique=? where id=?"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

                requetePreparer?.setString(1, unTypeArme.nom)
                requetePreparer?.setString(2, unTypeArme.nombreDes)
                requetePreparer?.setInt(3, unTypeArme.valeurDeMax)
                requetePreparer?.setInt(4, unTypeArme.multiplicateurCritique)
                requetePreparer?.setInt(5, unTypeArme.activationCritique)
                requetePreparer?.setInt(6, unTypeArme.id!!)
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
                    unTypeArme.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                    return unTypeArme
                }
            }
            requetePreparer.close()
        }
        else{
            println("Le TypeArme ${unTypeArme.nom} existe deja dans la bdd.")
        }
        return null
    }

    /**
     * Sauvegarde toutes les TypeArme dans la liste dans la base de données.
     *
     * @param lesTypeArmes La liste des objets TypeArme à sauvegarder.
     * @return Une liste des objets typearme sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesTypeArmes:Collection<TypeArme>):MutableMap<String,TypeArme>{
        var result= mutableMapOf<String,TypeArme>()
        for (unTypeArme in lesTypeArmes){
            val typeArmeSauvegarde=this.save(unTypeArme)
            if (typeArmeSauvegarde!=null){
                result.set(typeArmeSauvegarde.nom.lowercase(),typeArmeSauvegarde)
            }
        }
        return result
    }

    /**
     * Supprime un typearme de la base de données en fonction de son ID.
     *
     * @param id L'ID de le typearme à supprimer.
     * @return `true` si le typearme a été supprimée avec succès, sinon `false`.
     */
    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM TypeArme WHERE id = ?"
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
            println("Une erreur est survenue lors de la suppression de le typeArme : ${erreur.message}")
            return false
        }
    }

}
