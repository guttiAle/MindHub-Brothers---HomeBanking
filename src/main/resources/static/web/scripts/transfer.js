const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            div1: false,
            div2: true
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/current')
        .then(response =>{
            this.data = response.data
            console.log(response.data)
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
            })},
        create(){
            axios.post('/api/clients/current/accounts')
            .then(response => window.location.replace('./accounts.html'))
            .catch(error => {console.error(error)})
        },
        mostrarDiv1() {
            this.div1 = true;
            this.div2 = false;
        },
        mostrarDiv2() {
            this.div2 = true;
            this.div1 = false;
        }
    }
}).mount("#app")