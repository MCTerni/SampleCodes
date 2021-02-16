import { useState, useEffect } from "react";
import ListGroup from "react-bootstrap/ListGroup";
import MovieData from "../models/MovieData";
import api from "../services/omdb-api";
import { useTranslation } from 'react-i18next';
import ListItem from './ListItem';


function ResultList(props) {
    const [list, setList] = useState([]);
    const [favoriteImdbIDs, setFavoriteImdbIDs] = useState([]);
    const {t, i18n} = useTranslation();

    useEffect(() => {
        api.get(props.searchFor).then(response => {
            if (response.data.Search)
                setList(response.data.Search);
            //clean the list if the error is Too many results
            if (response.data.Error === 'Too many results.')
                setList([]);

        })

        //update favoriteImdbIDs
        try {
            favoriteImdbIDs.splice(0, favoriteImdbIDs.length);
            props.favoriteMovies.forEach(e => {
                favoriteImdbIDs.push(e.imdbID)
            });
        } catch { }


    }, [props.searchFor, props.favoriteMovies]);

    return (
        <ListGroup >
            { list.map((element, index) => {
                return (
                    <ListItem id={index}
                        movieInfo={new MovieData(element)}
                        onClick={props.onClick}
                        btnText={t('Nominate')}
                        btnDisabled={favoriteImdbIDs.includes(element.imdbID)}
                    />
                );
            })}
        </ListGroup>
    );
}

export default ResultList;
