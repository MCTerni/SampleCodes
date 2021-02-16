import { useState, useEffect } from "react";
import ListGroup from "react-bootstrap/ListGroup";
import Toast from 'react-bootstrap/Toast'
import { useTranslation } from 'react-i18next';
import ListItem from "./ListItem";



const storagedMovieInfo = JSON.parse(localStorage.getItem('favoriteMovies')) || [];

function FavoriteList(props) {

    const [list] = useState(storagedMovieInfo || []);
    const [listSize, setListSize] = useState(storagedMovieInfo.length || 0);
    const [showToast, setShowToast] = useState(false);
    const {t, i18n} = useTranslation();
    

    useEffect(() => {
        if (props.movie && listSize < 5) {
            list.push(props.movie);
            setListSize(prevSize => prevSize + 1);
            localStorage.setItem('favoriteMovies', JSON.stringify(list));
        }

        if (list.length >= 5) {
            setShowToast(true);
        }
    }, [props.movie]);

    const handleRemove = (e) => {
        list.splice(e.target.parentNode.id, 1);
        setListSize(prevSize => prevSize - 1);
        localStorage.setItem('favoriteMovies', JSON.stringify(list));
    }

    return (
        <div>
            <Toast onClose={() => setShowToast(false)} show={showToast} delay={5000} autohide>
                <Toast.Header>
                    <img src="holder.js/20x20?text=%20" className="rounded mr-2" alt="" />
                    <strong>{t("You've added 5 movies to your list")}</strong>
                </Toast.Header>
            </Toast>
            <ListGroup >
                {list.map((element, index) => {
                    return (
                        <ListItem id={index}
                            movieInfo={element}
                            onClick={handleRemove}
                            btnText={t('Remove')}
                            btnDisabled={false}
                        />
                    );
                })}
            </ListGroup>
        </div>
    );
}

export default FavoriteList;
