const {createApp} = Vue
createApp({
    data(){
        return{
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            error: null,
        }
    },
    methods: {
        register() {
                const data = `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`;
                axios.post('/api/clients', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
                })
                .then(
                    response => {
                        axios.post('/api/login', `email=${this.email}&password=${this.password}`, {headers: {'content-type': 'application/x-www-form-urlencoded'}})
                        .then(response => {
                            window.location.replace('./accounts.html');
                        })})
                .catch(error => {
                console.error(error);
                this.error = 'Failed to register. Please try again.';
                });
    }
}}).mount("#app")