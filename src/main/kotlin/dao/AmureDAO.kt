package model.jeu.dao
import coBDD
import jdbc.BDD
import model.item.Armure
import qualiteDAO
import typeArmureDAO
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement


class AmureDAO {
    /**
     * Cette classe représente un repository pour les objets Armure, permettant d'effectuer des opérations de
     * recherche et de sauvegarde dans la base de données.
     *
     * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
     */
    class ArmureDAO(val bdd: BDD = coBDD) {

        /**
         * Recherche et retourne toutes les armures de la base de données.
         *
         * @return Une liste de toutes les armures trouvées.
         */
        fun findAll(): MutableMap<String, Armure> {
            val result = mutableMapOf<String, Armure>()

            val sql = "SELECT * FROM Armure"
            val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
            val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
            if (resultatRequete != null) {
                while (resultatRequete.next()) {
                    val id = resultatRequete.getInt("id")
                    val nom = resultatRequete.getString("nom")
                    val description = resultatRequete.getString("description")
                    val idTypeArmure = resultatRequete.getInt("id_type")
                    val idQualite = resultatRequete.getInt("id_qualite")
                    val typeArmure = typeArmureDAO.findById(idTypeArmure)
                    val qualite = qualiteDAO.findById(idQualite)

                    result.set(nom.lowercase(), Armure(id, nom, description, typeArmure, qualite))
                }
            }
            requetePreparer.close()
            return result
        }

    }
}