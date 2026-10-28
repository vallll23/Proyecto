package cl.duoc.psicoevalua.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Rol(val etiqueta: String) {
    PSICOLOGO("Psicólogo PIE"),
    ESTUDIANTE("Estudiante")
}

data class Usuario(
    val id: Long,
    val nombre: String,
    val correo: String,
    val rol: Rol
)

sealed class ResultadoRegistro {
    object Exito : ResultadoRegistro()
    data class Error(val mensaje: String) : ResultadoRegistro()
}


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, NOMBRE_DB, null, VERSION_DB) {

    companion object {
        const val NOMBRE_DB = "psicoevalua.db"
        const val VERSION_DB = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                clave_hash TEXT NOT NULL,
                salt TEXT NOT NULL,
                rol TEXT NOT NULL,
                creado_en TEXT NOT NULL
            )
            """.trimIndent()
        )

        insertarUsuario(db, "Psicólogo Demo", "psicologo@mpaz.cl", "Demo1234", Rol.PSICOLOGO)
        insertarUsuario(db, "Estudiante Demo", "estudiante@mpaz.cl", "Demo1234", Rol.ESTUDIANTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }



    fun registrarUsuario(nombre: String, correo: String, clave: String, rol: Rol): ResultadoRegistro {
        val correoNormalizado = correo.trim().lowercase(Locale.ROOT)
        val db = writableDatabase
        val existe = db.rawQuery(
            "SELECT id FROM usuarios WHERE correo = ?",
            arrayOf(correoNormalizado)
        ).use { it.moveToFirst() }
        if (existe) {
            return ResultadoRegistro.Error("Ya existe una cuenta con ese correo.")
        }
        val id = insertarUsuario(db, nombre, correoNormalizado, clave, rol)
        return if (id != -1L) ResultadoRegistro.Exito
        else ResultadoRegistro.Error("No se pudo crear la cuenta. Intenta nuevamente.")
    }

    fun autenticar(correo: String, clave: String): Usuario? {
        val correoNormalizado = correo.trim().lowercase(Locale.ROOT)
        val db = readableDatabase
        return db.rawQuery(
            "SELECT id, nombre, correo, clave_hash, salt, rol FROM usuarios WHERE correo = ?",
            arrayOf(correoNormalizado)
        ).use { cursor ->
            if (!cursor.moveToFirst()) {
                null
            } else {
                val hashGuardado = cursor.getString(3)
                val salt = cursor.getString(4)
                if (hashClave(clave, salt) == hashGuardado) {
                    Usuario(
                        id = cursor.getLong(0),
                        nombre = cursor.getString(1),
                        correo = cursor.getString(2),
                        rol = Rol.valueOf(cursor.getString(5))
                    )
                } else {
                    null
                }
            }
        }
    }



    private fun insertarUsuario(
        db: SQLiteDatabase,
        nombre: String,
        correo: String,
        clave: String,
        rol: Rol
    ): Long {
        val salt = generarSalt()
        val valores = ContentValues().apply {
            put("nombre", nombre.trim())
            put("correo", correo.trim().lowercase(Locale.ROOT))
            put("clave_hash", hashClave(clave, salt))
            put("salt", salt)
            put("rol", rol.name)
            put("creado_en", SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date()))
        }
        return db.insert("usuarios", null, valores)
    }

    private fun generarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun hashClave(clave: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest((salt + clave).toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
