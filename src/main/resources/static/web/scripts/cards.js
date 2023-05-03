const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            creditCards: [],
            debitCards: []
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/current')
        .then(response =>{
            this.data = response.data
            this.creditCards = this.data.cards.filter((card) => card.type === 'CREDIT')
            this.debitCards = this.data.cards.filter((card) => card.type === 'DEBIT')
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