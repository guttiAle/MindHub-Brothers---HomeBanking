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
            // axios
            //     .post('/api/logout')
            //     .then(response => {
            //     window.location.replace('./index.html');
            // })
            // .catch(error => {
            //     console.error(error);
            // })}
            Swal.fire({
                title: 'Sure you want to log out?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                    .post('/api/logout')
                    .then(response => {window.location.replace('./index.html')})
                    .catch(error => {
                        if (error.response.status === 403) {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                            })
                        } else {
                            console.log(error)
                        }
                    })
                }
            })},
    }
}).mount("#app")