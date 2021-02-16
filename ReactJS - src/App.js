import { useState, useEffect } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { useTranslation } from 'react-i18next';
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import SearchBox from "./components/SearchBox";
import ResultList from "./components/ResultList";
import FavoriteList from "./components/FavoriteList";
import LanguagesDropDown from "./components/LanguagesDropDown";
import "./styles/custom.css";

function App() {
  const [searchFor, setSearchFor] = useState("");
  const [movieData, setMovieData] = useState();
  const [favoriteMovies, setFavoriteMovies] = useState([]);
  const { t, i18n } = useTranslation();

  useEffect(() => {
    try {
      setFavoriteMovies(JSON.parse(localStorage.getItem("favoriteMovies")));
    } catch {
      setFavoriteMovies([]);
    }
  }, [movieData, searchFor]);

  return (
    <div className="App">
      <Container fluid="md">
        <header className="header">
          {t('The Shoppies')}
          <div className="langDropDown">
            {t('Select Language')}
            <LanguagesDropDown />
          </div>
        </header>
        <div className="serch-box">
          <SearchBox onChange={(e) => setSearchFor(e.target.value)} />
        </div>
        <Row xs={1} md={2} className="lists">
          <Container>
            <Col className="list-block">
              <h3 className="list-header">{t('Nominations')}</h3>
              <Container>
                <FavoriteList movie={movieData} />
              </Container>
            </Col>
          </Container>
          <Container>
            <Col className="list-block">
              <h3 className="list-header">{t('Results for')} "{searchFor}"</h3>
              <div>
                <ResultList
                  id="result-list"
                  searchFor={searchFor}
                  //onClick={ (e)=>setFavMovie(e.target.parentNode.childNodes[1].data) }
                  onClick={(e) =>
                    setMovieData(JSON.parse(e.target.dataset.moviedata))
                  }
                  favoriteMovies={favoriteMovies}
                />
              </div>
            </Col>
          </Container>
        </Row>
      </Container>
    </div>
  );
}

export default App;
