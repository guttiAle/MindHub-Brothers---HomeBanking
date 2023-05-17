const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            firstName: "",
            lastName: "",
            email: "",
            loanName: "",
            amount: undefined,
            interest: undefined,
            payments: undefined,
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
            })},
        createNewLoan() {
            Swal.fire({
                title: 'Sure you want to create the loan?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.isConfirmed) {
                    const cuotas = (this.payments).split(',').map(numero => parseInt(numero))
                    console.log(cuotas)
                    axios
                    .post('/api/loans/create',{ name: this.loanName, amount: this.amount, interest: this.interest, payments: cuotas})
                    .then(response => {
                        console.log(response)
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
            })},
    }
}).mount("#app")