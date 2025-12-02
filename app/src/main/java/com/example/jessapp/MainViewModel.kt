import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jessapp.Cast
import com.example.jessapp.Movie
import com.example.jessapp.Person
import com.example.jessapp.Series
import com.example.jessapp.TmdbAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel : ViewModel() {

    // Remplacer par votre VRAIE clé API ici
    private val apikey = "921cce22156ad67dc3690d7c881e4037"

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val api = retrofit.create(TmdbAPI::class.java)

    // --- ETATS (StateFlows) ---
    // Notez l'utilisation de Movie, Series, Person définis dans votre Model.kt
    val movies = MutableStateFlow<List<Movie>>(listOf())
    val series = MutableStateFlow<List<Series>>(listOf())
    val actors = MutableStateFlow<List<Person>>(listOf())
    val selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedSeries = MutableStateFlow<Series?>(null)
    val currentCast = MutableStateFlow<List<Cast>>(emptyList())

    // --- FILMS ---
    fun getMovies() {
        viewModelScope.launch {
            try {
                movies.value = api.getLastMovies(apikey).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                movies.value = api.searchMovies(apikey, query).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            try {
                selectedMovie.value = api.getMovieDetail(id, apikey)
                currentCast.value = api.getMovieCredits(id, apikey).cast
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // --- SÉRIES ---
    fun getSeries() {
        viewModelScope.launch {
            try {
                series.value = api.getLastSeries(apikey).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchSeries(query: String) {
        viewModelScope.launch {
            try {
                series.value = api.searchSeries(apikey, query).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSeriesDetail(id: Int) {
        viewModelScope.launch {
            try {
                selectedSeries.value = api.getSeriesDetail(id, apikey)
                currentCast.value = api.getSeriesCredits(id, apikey).cast
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // --- ACTEURS ---
    fun getActors() {
        viewModelScope.launch {
            try {
                actors.value = api.getLastPersons(apikey).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchActors(query: String) {
        viewModelScope.launch {
            try {
                actors.value = api.searchPersons(apikey, query).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}