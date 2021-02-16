import {useState} from 'react';
import { useTranslation } from 'react-i18next';
import { keys } from '../i18n'

export default function LanguagesDropDown() {

    const { i18n } = useTranslation();
    const [language, setLanguage] = useState(i18n.language);

    function handleChangeLanguage(event) {
        if (event.target.value) {
            i18n.changeLanguage(event.target.value);
            setLanguage(i18n.language);
            localStorage.setItem('language',i18n.language);
        }
    }

    return (
        <div>
            <select id='language' value={language} onChange={handleChangeLanguage}>
                <option value={keys[0]}>English</option>
                <option value={keys[1]}>Français</option>
            </select>
        </div>
    )
}
