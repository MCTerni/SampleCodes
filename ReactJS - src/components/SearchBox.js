
import {InputGroup, FormControl} from "react-bootstrap";
import {BsSearch} from "react-icons/bs";
import { useTranslation } from 'react-i18next';

function SearchBox(props) {
    const {t, i18n} = useTranslation();
    return (
        <InputGroup className="mb-3">
            <InputGroup.Prepend>
                <InputGroup.Text><BsSearch/></InputGroup.Text>
            </InputGroup.Prepend>
            <FormControl
                placeholder={t("Movie Title")}
                aria-label="Recipient's username"
                aria-describedby="basic-addon2"
                onChange = {props.onChange}
            />
        </InputGroup>
    );
}

export default SearchBox;