const {createApp} = Vue
let valorID = (new URLSearchParams(location.search)).get("id")
const url = `http://localhost:8080/api/accounts/${valorID}`

createApp({
    data(){
        return{
            data: []
        }
    },
    created(){
        axios.get(url)
        .then(response =>{
            this.data = response.data.transactions
            this.data.sort((a, b) => b.id - a.id)
            console.log(response.data.transactions.sort((a, b) => b.id - a.id))
        })
        .catch(err => console.log(err))
    },
    methods: {
        logout() {
            axios
                .post('/api/logout')
                .then(response => {
                window.location.replace('./index.html');
            })
            .catch(error => {
                console.error(error);
            })}
    }
}).mount("#app")