export default class MovieData {
    constructor(jsonData) {
        this.title = jsonData.Title;
        this.year = jsonData.Year;
        this.imdbID = jsonData.imdbID;
        this.poster = jsonData.Poster;
    }
}