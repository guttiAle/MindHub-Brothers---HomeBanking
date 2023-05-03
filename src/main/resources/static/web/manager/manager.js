const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            firstName: "",
            lastName: "",
            email: "",
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients')
        .then(response =>{
            this.data = response.data
        })
        .catch(err => console.log(err))
    },
    methods: {
        logout() {
            axios
                .post('/api/logout')
                .then(response => {
                window.location.replace('/web/index.html');
            })
            .catch(error => {
                console.error(error);
            })},

        h2console() {
            axios
            .post(window.location.replace('/web/index.html'))
            .catch(error => {
                console.error(error);
            })}
    }
}).mount("#app")