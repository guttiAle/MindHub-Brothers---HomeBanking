const {createApp} = Vue
let valorID = (new URLSearchParams(location.search)).get("id")
// const url = `http://localhost:8080/api/accounts/${valorID}`
const url = `http://localhost:8080/api/clients/current`

createApp({
    data(){
        return{
            data: [],
            accounts: [],
            account: []
        }
    },
    created(){
        axios.get(url)
        .then(response =>{
            this.accounts = response.data.accounts
            this.filter(this.accounts)
            this.account.transactions.sort((a, b) => b.id - a.id)
            console.log(this.account.transactions.sort((a, b) => b.id - a.id))

        })
        .catch(err => console.log(err))
    },
    methods: {
        logout() {
            axios
                .post('/api/logout')
                .then(response => {
                window.location.replace('./index.html')
            })
            .catch(error => {
                console.error(error)
            })
        },
        filter(list) {
            for (let i = 0; i < list.length; i++) {
                if(list[i].id == valorID){

                    this.account = list[i]
                }
            }
        }
    }
}).mount("#app")
