const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            availableLoans: undefined,
            activeClientLoan: undefined
        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/current')
        .then(response =>{
            this.data = response.data
            // console.log(response.data)
        })
        .catch(err => console.log(err))
        axios.get('http://localhost:8080/api/loans')
        .then(response =>{
            this.availableLoans = response.data
            // console.log(response.data)
        })
        axios.get('http://localhost:8080/api/current-loans')
        .then(response => {
            this.activeClientLoan = response.data
            console.log(response.data)
        })
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
        async create(){
                const inputOptions = new Promise((resolve) => {
                    setTimeout(() => {
                    resolve({
                        'CHECKING': 'Checking account',
                        'SAVINGS': 'Savings account'
                    })
                    }, 1000)
                })
                
                const { value: color } = await Swal.fire({
                    title: 'Select the type of account you want to create',
                    input: 'radio',
                    inputOptions: inputOptions,
                    inputValidator: (value) => {
                    if (!value) {
                        return 'You need to choose something!'
                    }
                    }
                })
                
                if (color) {
                    Swal.fire({ html: `You selected: ${color}` })
                    axios.post('/api/clients/current/accounts', `accountType=${color}`)
                    .then(response => window.location.replace('./accounts.html'))
                    .catch(error => {console.error(error)})
                }
        },
        delAccount(number){
            Swal.fire({
                title: 'Sure you want to delete the account?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/accounts/delete',`number=${number}`)
                    .then(response => {
                        window.location.replace('./accounts.html')
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