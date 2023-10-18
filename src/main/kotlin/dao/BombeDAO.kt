package model.jeu.dao
import coBDD
import jdbc.BDD
import model.item.Bombe
import model.item.Qualite
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

class BombeDAO(val bdd: BDD =coBDD) {
    fun findAll(): MutableMap<String, Bombe> {
        val result = mutableMapOf<String, Bombe>()

        val sql = "SELECT * FROM Bombe"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val nombreDeDes = resultatRequete.getInt("nombreDeDes")
                val maxDe = resultatRequete.getInt("maxDe")
                result.set(nom.lowercase(), Bombe(id, nom, description, nombreDeDes, maxDe))
            }
        }
        requetePreparer.close()
        return result

    }
    fun findByNom(nomRechecher:String): MutableMap<String,Bombe> {
        val result = mutableMapOf<String,Bombe>()

        val sql = "SELECT * FROM Qualite WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val nombreDeDes = resultatRequete.getInt("nombreDeDes")
                val maxDe = resultatRequete.getInt("maxDe")
                result.set(nom.lowercase(), Bombe(id, nom, description, nombreDeDes, maxDe))
            }
        }
        requetePreparer.close()
        return result
    }

    fun findById(id:Int): Bombe? {
        var result :Bombe?=null
        val sql = "SELECT * FROM Qualite WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val id = resultatRequete.getInt("id")
                val nom = resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val nombreDeDes = resultatRequete.getInt("nombreDeDes")
                val maxDe = resultatRequete.getInt("maxDe")
                result=Bombe(id, nom, description, nombreDeDes, maxDe)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }
    fun save(uneBombe: Bombe): Bombe? {
        val qualiteBdd=this.findByNom(uneBombe.nom)
        if(qualiteBdd.isEmpty()) {


            val requetePreparer: PreparedStatement

            if (uneBombe.id == null) {
                val sql =
                    "Insert Into Bombe (nom, description, nombreDeDes, maxDe) values (?,?,?,?)"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                requetePreparer?.setString(1, uneBombe.nom)
                requetePreparer?.setString(2, uneBombe.description)
                requetePreparer?.setInt(3, uneBombe.nombreDeDes)
                requetePreparer?.setInt(4, uneBombe.maxDe)

            } else {
                var sql = ""
                sql =
                    "Update  Qualite set nom=?,description=?,nombreDeDes=? ,maxDe=? where id=?"
                requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

                requetePreparer?.setString(1, uneBombe.nom)
                requetePreparer?.setString(2, uneBombe.description)
                requetePreparer?.setInt(3, uneBombe.nombreDeDes)
                requetePreparer?.setInt(4, uneBombe.maxDe)
                requetePreparer?.setInt(5, uneBombe.id!!)
            }

            val nbLigneMaj = requetePreparer?.executeUpdate()
            // La méthode executeUpdate() retourne le nombre de lignes modifié par un insert, update ou delete sinon elle retourne 0 ou -1

            // Si l'insertion a réussi
            if (nbLigneMaj != null && nbLigneMaj > 0) {
                // Récupérez les clés générées (comme l'ID auto-incrémenté)
                val generatedKeys = requetePreparer.generatedKeys
                if (generatedKeys.next()) {
                    val id = generatedKeys.getInt(1) // Supposons que l'ID est la première col
                    uneBombe.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                    return uneBombe
                }
            }
            requetePreparer.close()
        }
        else{
            println("La qualite ${uneBombe.nom} existe deja dans la bdd.")
        }
        return null
    }

    fun saveAll(lesBombe:Collection<Bombe>):MutableMap<String,Bombe>{
        var result= mutableMapOf<String,Bombe>()
        for (uneBombe in lesBombe){
            val potionSauvegarde=this.save(uneBombe)
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