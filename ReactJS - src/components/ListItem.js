import { useState, useEffect } from "react";
import ListGroupItem from "react-bootstrap/ListGroupItem";
import Button from "react-bootstrap/Button";



function ListItem(props) {
    const [movieData, setMovieData] = useState(props.movieInfo);

    useEffect(() => {
        setMovieData(props.movieInfo);
    }, [props.movieInfo])

    return (
        <ListGroupItem id={props.id} >
            <img src={movieData.poster} height='80rem' alt='No Poster   ' className='poster' />
            <a href={`https://www.imdb.com/title/${movieData.imdbID}`} target="_blank" rel="noreferrer">
                {`${movieData.title} (${movieData.year}) `}
            </a>
            <br />
            <Button className='btnItem'
                variant='dark'
                size='sm'
                onClick={props.onClick}
                data-moviedata={JSON.stringify(movieData)}
                disabled={props.btnDisabled}
            >
                {props.btnText}
            </Button>
        </ListGroupItem>
    )
}

export default ListItem;