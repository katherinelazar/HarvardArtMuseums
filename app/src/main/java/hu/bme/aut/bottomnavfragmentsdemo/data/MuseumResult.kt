package hu.bme.aut.bottomnavfragmentsdemo.data

// result generated from /json

data class Base(val info: Info?, val records: List<Records>?)

data class Info(val totalrecordsperquery: Number?, val totalrecords: Number?,
                val pages: Number?, val page: Number?, val next: String?)

data class Records(val accessionyear: Any?, val technique: String?, val mediacount: Number?,
                   val edition: Any?, val totalpageviews: Number?, val groupcount: Number?,
                   val people: List<People>?, val objectnumber: String?,
                   val colorcount: Number?, val lastupdate: String?, val rank: Number?,
                   val imagecount: Number?, val description: Any?, val dateoflastpageview: String?,
                   val dateoffirstpageview: String?, val primaryimageurl: String?,
                   val colors: List<Colors>?, val dated: String?,
                   val contextualtextcount: Number?, val copyright: Any?,
                   val period: Any?, val accessionmethod: String?, val url: String?,
                   val provenance: String?, val images: List<Images>?,
                   val publicationcount: Number?, val objectid: Number?, val culture: String?,
                   val verificationleveldescription: String?, val standardreferencenumber: String?,
                   val worktypes: List<Worktypes>?, val department: String?,
                   val state: Any?, val markscount: Number?, val contact: String?,
                   val titlescount: Number?, val id: Number?, val title: String?,
                   val verificationlevel: Number?, val division: String?, val style: Any?,
                   val commentary: Any?, val relatedcount: Number?, val datebegin: Number?,
                   val labeltext: Any?, val totaluniquepageviews: Number?, val dimensions: Any?,
                   val exhibitioncount: Number?, val techniqueid: Number?, val dateend: Number?,
                   val creditline: String?, val imagepermissionlevel: Number?, val signed: Any?,
                   val periodid: Any?, val century: String?, val classificationid: Number?,
                   val medium: String?, val peoplecount: Number?, val accesslevel: Number?,
                   val classification: String?, val seeAlso: List<SeeAlso>?)

data class SeeAlso(val id: String?, val type: String?, val format: String?, val profile: String?)

data class Colors(val percent: Number?, val spectrum: String?, val color: String?,
                  val css3: String?, val hue: String?)

data class People(val alphasort: String?, val birthplace: String?, val name: String?,
                  val prefix: Any?, val personid: Number?, val gender: String?,
                  val role: String?, val displayorder: Number?, val culture: String?,
                  val displaydate: String?, val deathplace: String?, val displayname: String?)

data class Worktypes(val worktypeid: String?, val worktype: String?)

data class Images(val height: Number?, val iiifbaseuri: String?,
                  val baseimageurl: String?, val width: Number?, val publiccaption: Any?,
                  val idsid: Number?, val displayorder: Number?, val format: String?,
                  val copyright: String?, val imageid: Number?, val renditionnumber: String?)

