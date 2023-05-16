const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            creditCards: [],
            debitCards: [],
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/current/cards')
        .then(response =>{
            this.data = response.data
            console.log(response.data)
            this.creditCards = this.data.filter((card) => card.type === 'CREDIT')
            this.debitCards = this.data.filter((card) => card.type === 'DEBIT')
        })
        .catch(err => console.log(err))
    },
    methods: {
        logout() {
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
            deleteCard(number) {
                Swal.fire({
                    title: 'Sure you want to delete the card?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes'
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post('/api/clients/current/cards/delete',`number=${number}`)
                        .then(response => {
                            window.location.replace('./cards.html')
                        })
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
                })
            }
    }
}).mount("#app")